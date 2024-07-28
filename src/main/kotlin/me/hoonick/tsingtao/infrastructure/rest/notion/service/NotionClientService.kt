package me.hoonick.tsingtao.infrastructure.rest.notion.service

import me.hoonick.tsingtao.infrastructure.rest.notion.dto.*
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd")

@Service
class NotionClientService(
    private val notionClient: NotionClient,
) {


    fun createPage() {
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
                        TitleElement(text = Text(content = LocalDate.now().format(DATE_FORMAT))),
                    )
                )
            ),
            children = listOf(
                Block(
                    heading_2 = Heading2(
                        rich_text = listOf(
                            RichTextContent(text = TextContent(content = "\uD83D\uDFE9 Todo"))
                        ),
                        children = listOf(
                            Block(
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
                Block(
                    heading_2 = Heading2(
                        rich_text = listOf(
                            RichTextContent(text = TextContent(content = "✅ Done"))
                        ),
                        children = listOf(
                            Block(
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
                Block(
                    heading_2 = Heading2(
                        rich_text = listOf(
                            RichTextContent(text = TextContent(content = "❓ Question"))
                        ),
                        children = listOf(
                            Block(
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
                Block(
                    heading_2 = Heading2(
                        rich_text = listOf(
                            RichTextContent(text = TextContent(content = "\uD83D\uDC4B Backlog"))
                        ),
                        children = listOf(
                            Block(
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
                Block(
                    heading_2 = Heading2(
                        rich_text = listOf(
                            RichTextContent(text = TextContent(content = "\uD83E\uDEA9 Event"))
                        ),
                        children = listOf(
                            Block(
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
        val response = notionClient.createPage(request)
        println()
    }


}