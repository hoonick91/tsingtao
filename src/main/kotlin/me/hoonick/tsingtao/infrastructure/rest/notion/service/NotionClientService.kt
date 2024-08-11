package me.hoonick.tsingtao.infrastructure.rest.notion.service

import com.fasterxml.jackson.databind.JsonNode
import me.hoonick.tsingtao.domain.Block
import me.hoonick.tsingtao.domain.ChildPage
import me.hoonick.tsingtao.domain.Detail
import me.hoonick.tsingtao.domain.port.out.NotionPort
import me.hoonick.tsingtao.infrastructure.rest.notion.dto.BlockResponse
import me.hoonick.tsingtao.infrastructure.rest.notion.dto.PageCreateRequest
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class NotionClientService(
    private val notionClient: NotionClient,
) : NotionPort {

    override fun getChildPagesInBlocks(pageId: String): List<ChildPage> {
        val response = notionClient.getBlocks(pageId)
        return response.toChildPages()
    }

    override fun getBlocks(pageId: String): JsonNode {
        return notionClient.getBlocks(pageId)
    }

    override fun getBlocks(pageId: String, fieldName: String): List<Block> {
        val response = notionClient.getBlocks(pageId)
        return response.toBlocks(fieldName)
    }

    override fun createPage(blocks: Map<String, List<Block>>) {
        val request = PageCreateRequest(
            children = listOf(
                BlockResponse.todo(blocks),
                BlockResponse.done(blocks),
                BlockResponse.question(blocks),
                BlockResponse.information(blocks),
                BlockResponse.backlog(blocks),
                BlockResponse.event(blocks),
            )
        )
        notionClient.createPage(request)
    }

    override fun updateBlock(doneBlockId: String, doneContent: List<Block>) {
        val request = BlackAddChildrenRequest(
            children = doneContent.map {
                BlockRequest(
                    to_do = ToDoRequest(
                        rich_text = listOf(
                            RichTextRequest(text = TextContentRequest(content = it.detail.title)),
                        ),
                        checked = true
                    ),
                )
            },
        )
        notionClient.updateBlock(doneBlockId, request)
    }

    override fun deleteBlock(todoBlockId: String, doneContent: List<Block>) {
        doneContent.forEach {
            notionClient.deleteBlockBy(it.id)
        }
    }


}

private fun JsonNode.toBlocks(fieldName: String): List<Block> {
    return this.get("results")
        .filter { it.get("type").asText() == fieldName }
        .map {
            Block(
                id = it.get("id").asText(),
                createdAt = it.get("created_time").toLocalDateTime(),
                type = it.get("type").asText(),
                detail = Detail(it.get(fieldName))
            )
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
