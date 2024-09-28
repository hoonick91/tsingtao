package me.hoonick.tsingtao.notion.infrastructure.rest.notion.service


data class TextContentRequest(
    val content: String,
)

data class RichTextRequest(
    val text: TextContentRequest,
)

data class ToDoRequest(
    val rich_text: List<RichTextRequest>,
    val checked: Boolean,
    val color: String = "default",
)

data class BlockRequest(
    val `object`: String = "block",
    val to_do: ToDoRequest? = null,
)

data class BlackAddChildrenRequest(
    val children: List<BlockRequest>,
)