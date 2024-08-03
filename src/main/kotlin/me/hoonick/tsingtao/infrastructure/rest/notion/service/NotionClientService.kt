package me.hoonick.tsingtao.infrastructure.rest.notion.service

import com.fasterxml.jackson.databind.JsonNode
import me.hoonick.tsingtao.domain.Block
import me.hoonick.tsingtao.domain.ChildPage
import me.hoonick.tsingtao.domain.Detail
import me.hoonick.tsingtao.domain.port.out.NotionPort
import me.hoonick.tsingtao.infrastructure.rest.notion.dto.*
import me.hoonick.tsingtao.infrastructure.rest.notion.dto.Properties
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

private val DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd")

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
        val today = LocalDate.now()
        val dayOfWeek = today.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)
        val title = today.format(DATE_FORMAT) + "($dayOfWeek)"

        val request = PageCreateRequest(
            parent = Parent(page_id = "c63366bc-abdd-47b0-b463-db86acff3e49"),
            icon = Icon(emoji = "\uD83D\uDD5B"),
            cover = Cover(
                external = External(
                    url = "https://upload.wikimedia.org/wikipedia/commons/6/62/Tuscankale.jpg"
                )
            ),
            properties = Properties(
                title = Title(
                    title = listOf(
                        TitleElement(text = Text(content = title)),
                    )
                )
            ),
            children = listOf(
                BlockResponse(
                    heading_2 = Heading2(
                        rich_text = listOf(
                            RichTextContent(text = TextContent(content = "\uD83D\uDFE9 Todo"))
                        ),
                        children =
                        blocks["Todo"]?.map {
                            BlockResponse(
                                to_do = ToDo(
                                    rich_text = listOf(
                                        RichTextContent(text = TextContent(content = "${it.detail.title} #${it.createdAt.toLocalDate()}"))
                                    ),
                                    checked = false,
                                )
                            )
                        }
                    )
                ),
                BlockResponse(
                    heading_2 = Heading2(
                        rich_text = listOf(
                            RichTextContent(text = TextContent(content = "✅ Done"))
                        ),
                        children = listOf(
                            BlockResponse(
                                to_do = ToDo(
                                    rich_text = listOf(
                                        RichTextContent(text = TextContent())
                                    ),
                                    checked = false,
                                )
                            )
                        )
                    )
                ),
                BlockResponse(
                    heading_2 = Heading2(
                        rich_text = listOf(
                            RichTextContent(text = TextContent(content = "❓ Question"))
                        ),
                        children = blocks["Question"]?.map {
                            BlockResponse(
                                to_do = ToDo(
                                    rich_text = listOf(
                                        RichTextContent(text = TextContent(content = "${it.detail.title} #${it.createdAt.toLocalDate()}"))
                                    ),
                                    checked = false,
                                )
                            )
                        }
                    )
                ),
                BlockResponse(
                    heading_2 = Heading2(
                        rich_text = listOf(
                            RichTextContent(text = TextContent(content = "\uD83D\uDC4B Backlog"))
                        ),
                        children = listOf(
                            BlockResponse(
                                to_do = ToDo(
                                    rich_text = listOf(
                                        RichTextContent(text = TextContent())
                                    ),
                                    checked = false,
                                )
                            )
                        )
                    )
                ),
                BlockResponse(
                    heading_2 = Heading2(
                        rich_text = listOf(
                            RichTextContent(text = TextContent(content = "\uD83E\uDEA9 Event"))
                        ),
                        children = listOf(
                            BlockResponse(
                                to_do = ToDo(
                                    rich_text = listOf(
                                        RichTextContent(text = TextContent())
                                    ),
                                    checked = false,
                                )
                            )
                        )
                    )
                )
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
                hasChildren = it.get("has_children").asBoolean(),
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
