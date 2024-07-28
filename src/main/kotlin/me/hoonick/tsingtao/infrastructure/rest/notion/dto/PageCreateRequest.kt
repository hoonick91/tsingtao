package me.hoonick.tsingtao.infrastructure.rest.notion.dto

data class PageCreateRequest(
    val parent: Parent,
    val icon: Icon,
    val cover: Cover,
    val properties: Properties,
    val children: List<Block>,
)

data class TextContent(
    val content: String? = "ㅇㅇㅇ",
)

data class RichTextContent(
    val text: TextContent,
)

data class ToDo(
    val rich_text: List<RichTextContent>,
    val checked: Boolean,
    val color: String? = "default",
)

data class Heading2(
    val rich_text: List<RichTextContent>,
    val color: String? = "default",
    val children: List<Block>? = null,
)

data class Block(
    val heading_2: Heading2? = null,
    val to_do: ToDo? = null,
)

