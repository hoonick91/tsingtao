package me.hoonick.tsingtao.notion.infrastructure.rest.notion.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.contains
import me.hoonick.tsingtao.notion.domain.ChildPage
import me.hoonick.tsingtao.notion.domain.*
import me.hoonick.tsingtao.notion.domain.port.out.NotionPort
import me.hoonick.tsingtao.notion.infrastructure.rest.notion.dto.*
import me.hoonick.tsingtao.notion.infrastructure.rest.notion.dto.BlockRequest
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Service
class NotionClientService(
    private val notionClient: NotionClient,
) : NotionPort {

    override fun saveDailyPage(contents: DailyContents) {
        val request = PageSaveRequest(
            children = contents.contents.entries
                .map {
                    BlockRequest(
                        toggle = Toggle(
                            rich_text = listOf(
                                RichTextContent(text = TextContent(content = it.key.title))
                            ),
                            children = if (it.key.maintainChildren) it.value.toChildren() else listOf(BlockRequest.empty())
                        )
                    )
                }
        )
        notionClient.savePage(request)
    }


    override fun getChildrenBlocks(pageId: String): DailyPage {
        val response = notionClient.getBlocks(pageId)
        return DailyPage(blocks =
        response.get("results")
            .map {
                Block(
                    id = it.get("id").asText(),
                    parentId = getParentId(it),
                    createdAt = LocalDateTime.parse(
                        it.get("created_time").asText(),
                        DateTimeFormatter.ISO_OFFSET_DATE_TIME
                    ),
                    hasChildren = it.get("has_children").asBoolean(),
                    type = BlockType.valueOf(it.get("type").asText()),
                    contents = getContents(it, BlockType.valueOf(it.get("type").asText()))
                )
            }
        )
    }

    private fun getParentId(it: JsonNode): String {
        val parent = it.get("parent")
        if (parent.get("page_id") != null) return parent.get("page_id").asText()
        if (parent.get("block_id") != null) return parent.get("block_id").asText()
        throw IllegalStateException("no parent.")
    }

    private fun getContents(block: JsonNode, type: BlockType): Contents? {
        val contents = block.get(type.name) ?: return null
        if (contents.get("rich_text") != null) {
            val richText = contents.get("rich_text")
            val richTextFirst = richText.firstOrNull() ?: return null
            return Contents(
                type = ContentsType.valueOf(richTextFirst.get("type").asText()),
                checked = if (contents.contains("checked")) contents.get("checked").asBoolean() else null,
                text = richTextFirst.get("text").get("content").asText()
            )
        }

        if (contents.get("title") != null) {
            return Contents(type = ContentsType.text, text = contents.get("title").asText())
        }

        return Contents()
    }

    override fun getChildPagesInBlocks(pageId: String): List<ChildPage> {
        val response = notionClient.getBlocks(pageId)
        return response.toChildPages()
    }

    override fun getBlocks(pageId: String): JsonNode {
        return notionClient.getBlocks(pageId)
    }

    override fun savePageInDatabase(
        databaseId: String,
        outstandingAllChildBlocks: List<Block>,
    ) {
        outstandingAllChildBlocks
            .filter { it.contents != null }
            .map {
                PageCreateRequest2(
                    parent = Parent(
                        type = "database_id",
                        databaseId = databaseId
                    ),
                    properties = mapOf(
                        "Name" to Title(
                            title = listOf(
                                Title.TitleText(text = Title.TitleText.TextContent(content = it.contents!!.text))
                            )
                        )
                    ),
                    children = it.contents.childBlocks
                        ?.filter { child -> child.contents != null }
                        ?.map { child ->
                            mapOf(
                                child.type.name to BulletedListItem(
                                    richText = listOf(
                                        RichText(
                                            text = RichText.Text(
                                                content = child.contents!!.text
                                            )
                                        )
                                    ),
                                    children = child.contents.childBlocks
                                        ?.filter { grandChild -> grandChild.contents != null }
                                        ?.map { grandChild ->
                                            mapOf(
                                                grandChild.type.name to BulletedListItem(
                                                    richText = listOf(
                                                        RichText(
                                                            text = RichText.Text(
                                                                content = grandChild.contents!!.text
                                                            )
                                                        )
                                                    )
                                                )
                                            )
                                        }
                                )
                            )
                        } ?: emptyList()

                )
            }
            .forEach { request -> notionClient.createPage2(request) }
    }


}

fun List<Block>.toChildren(): List<BlockRequest> {
    return this.map { block ->
        when (block.type) {
            BlockType.to_do -> BlockRequest.toDo(block)
            BlockType.bulleted_list_item -> BlockRequest.bulletedListItem(block)
            else -> throw IllegalArgumentException()
        }
    }
}


private fun JsonNode.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.parse(this.asText(), DateTimeFormatter.ISO_DATE_TIME)
}

private fun JsonNode.toChildPages(): List<ChildPage> {
    return this.get("results")
        .filter { it.get("has_children").asBoolean() }
        .map {
            ChildPage(
                id = it.get("id").asText(),
                parentId = it.get("parent").get("page_id").asText(),
                createdAt = it.get("created_time").toLocalDateTime(),
                title = it.get("child_page").get("title").asText()
            )
        }
}
