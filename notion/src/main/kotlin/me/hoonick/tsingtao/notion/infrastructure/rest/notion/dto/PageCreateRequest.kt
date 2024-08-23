package me.hoonick.me.hoonick.tsingtao.notion.infrastructure.rest.notion.dto

import me.hoonick.me.hoonick.tsingtao.notion.domain.Block
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

private val DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd")

fun getTitle(): String {
    val today = LocalDate.now()
    val dayOfWeek = today.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)
    return today.format(DATE_FORMAT) + "($dayOfWeek)"
}

data class PageCreateRequest(
    val parent: Parent = Parent(page_id = "c63366bc-abdd-47b0-b463-db86acff3e49"),
    val icon: Icon = Icon(emoji = "\uD83D\uDD5B"),
    val cover: Cover = Cover(
        external = External(
            url = "https://upload.wikimedia.org/wikipedia/commons/6/62/Tuscankale.jpg"
        )
    ),
    val properties: Properties = Properties(
        title = Title(
            title = listOf(
                TitleElement(text = Text(content = getTitle())),
            )
        )
    ),
    val children: List<BlockResponse>,
)


data class TextContent(
    val content: String? = "ㅇㅇㅇ",
)

data class RichTextContent(
    val text: TextContent,
)

data class Item(
    val rich_text: List<RichTextContent>,
    val checked: Boolean? = null,
    val color: String? = "default",
    val children: List<BlockResponse>? = null,
)

data class Heading2(
    val rich_text: List<RichTextContent>,
    val color: String? = "default",
    val children: List<BlockResponse> = listOf(
        BlockResponse(
            to_do = Item(
                rich_text = listOf(
                    RichTextContent(text = TextContent())
                ),
                checked = false,
            )
        )
    ),
)

data class BlockResponse(
    val heading_2: Heading2? = null,
    val to_do: Item? = null,
    val bulleted_list_item: Item? = null,
    val children: List<BlockResponse>? = null,
) {
    companion object {
        fun todo(blocks: Map<String, List<Block>>): BlockResponse {
            return BlockResponse(
                heading_2 = Heading2(
                    rich_text = listOf(
                        RichTextContent(text = TextContent(content = "\uD83D\uDFE9 Todo"))
                    ),
                    children = blocks["Todo"]!!
                        .map { from(it) }
                )
            )
        }

        private fun from(it: Block): BlockResponse {
            return when (it.type) {
                "to_do" -> BlockResponse(
                    to_do = Item(
                        rich_text = listOf(
                            RichTextContent(text = TextContent(content = "${it.detail.title} #${it.createdAt.toLocalDate()}"))
                        ),
                        checked = false,
                        children = it.children?.map {
                            from(it)
                        }
                    ),

                    )

                "bulleted_list_item" -> BlockResponse(
                    bulleted_list_item = Item(
                        rich_text = listOf(
                            RichTextContent(text = TextContent(content = "${it.detail.title} #${it.createdAt.toLocalDate()}"))
                        ),
                        children = it.children?.map {
                            from(it)
                        }
                    ),
                )
                else -> BlockResponse()
            }
        }

        fun done(blocks: Map<String, List<Block>>): BlockResponse {
            return BlockResponse(
                heading_2 = Heading2(
                    rich_text = listOf(
                        RichTextContent(text = TextContent(content = "✅ Done"))
                    )
                )
            )
        }

        fun question(blocks: Map<String, List<Block>>): BlockResponse {
            return BlockResponse(
                heading_2 = Heading2(
                    rich_text = listOf(
                        RichTextContent(text = TextContent(content = "❓ Question"))
                    ),
                    children = blocks["Question"]!!.map {
                        BlockResponse(
                            to_do = Item(
                                rich_text = listOf(
                                    RichTextContent(text = TextContent(content = "${it.detail.title} #${it.createdAt.toLocalDate()}"))
                                ),
                                checked = false,
                            )
                        )
                    }
                )
            )
        }

        fun backlog(blocks: Map<String, List<Block>>): BlockResponse {
            return BlockResponse(
                heading_2 = Heading2(
                    rich_text = listOf(
                        RichTextContent(text = TextContent(content = "\uD83D\uDC4B Backlog"))
                    )
                )
            )
        }

        fun event(blocks: Map<String, List<Block>>): BlockResponse {
            return BlockResponse(
                heading_2 = Heading2(
                    rich_text = listOf(
                        RichTextContent(text = TextContent(content = "\uD83E\uDEA9 Event"))
                    )
                )
            )
        }

        fun information(blocks: Map<String, List<Block>>): BlockResponse {
            return BlockResponse(
                heading_2 = Heading2(
                    rich_text = listOf(
                        RichTextContent(text = TextContent(content = "\uD83D\uDCA1 Information"))
                    )
                )
            )
        }
    }
}

