package me.hoonick.tsingtao.notion.infrastructure.rest.notion.dto

import me.hoonick.tsingtao.notion.domain.Block
import me.hoonick.tsingtao.notion.infrastructure.rest.notion.service.toChildren
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

data class PageSaveRequest(
    val parent: Parent = Parent(page_id = "e389c8ec9ddc40988a1e9d71c88b68bd"),
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
    val children: List<BlockRequest>,
)


data class BlockRequest(
    val toggle: Toggle? = null,
    val heading_2: Heading2? = null,
    val to_do: Item? = null,
    val bulleted_list_item: Item? = null,
    val children: List<BlockRequest>? = null,
) {
    companion object {
        fun toDo(block: Block): BlockRequest {
            return BlockRequest(

                to_do = Item(
                    rich_text = listOf(
                        RichTextContent(text = TextContent(content = "${block.contents?.text}"))
                    ),
                    checked = block.contents?.checked,
                    children = if (block.hasChildren) block.contents?.childBlocks?.toChildren() else null
                )
            )
        }

        fun bulletedListItem(block: Block): BlockRequest {
            return BlockRequest(
                bulleted_list_item = Item(
                    rich_text = listOf(
                        RichTextContent(text = TextContent(content = "${block.contents?.text}"))
                    ),
                    children = if (block.hasChildren) block.contents?.childBlocks?.toChildren() else null
                )
            )
        }

        fun empty(): BlockRequest {
            return BlockRequest(
                to_do = Item(
                    rich_text = listOf(
                        RichTextContent(text = TextContent(content = "ㅇㅇㅇ"))
                    ),
                    checked = false
                )
            )
        }
    }


}

data class Parent(
    val type: String? = null,
    val page_id: String,
)

data class TextContent(
    val content: String? = "ㅇㅇㅇ",
)

data class RichTextContent(
    val text: TextContent,
)

data class Toggle(
    val rich_text: List<RichTextContent>,
    val color: String? = "default",
    val children: List<BlockRequest> = listOf(
        BlockRequest(
            to_do = Item(
                rich_text = listOf(
                    RichTextContent(text = TextContent())
                ),
                checked = false,
            )
        )
    ),
)

data class Heading2(
    val rich_text: List<RichTextContent>,
    val color: String? = "default",
    val children: List<BlockRequest> = listOf(
        BlockRequest(
            to_do = Item(
                rich_text = listOf(
                    RichTextContent(text = TextContent())
                ),
                checked = false,
            )
        )
    ),
)

data class Item(
    val rich_text: List<RichTextContent>,
    val checked: Boolean? = null,
    val color: String? = "default",
    val children: List<BlockRequest>? = null,
)

data class Icon(
    val type: String? = null,
    val emoji: String,
)

data class Cover(
    val type: String? = null,
    val external: External,
)

data class External(
    val url: String,
)

data class Title(
    val id: String? = null,
    val type: String? = null,
    val title: List<TitleElement>,
)

data class TitleElement(
    val type: String? = null,
    val text: Text,
    val annotations: Annotations? = null,
    val plain_text: String? = null,
    val href: String? = null,
)

data class Text(
    val content: String,
    val link: String? = null,
)

data class Annotations(
    val bold: Boolean,
    val italic: Boolean,
    val strikethrough: Boolean,
    val underline: Boolean,
    val code: Boolean,
    val color: String,
)

data class Properties(
    val title: Title,
)