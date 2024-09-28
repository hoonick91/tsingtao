package me.hoonick.tsingtao.notion.infrastructure.rest.notion.service

data class PageCreateRequest2(
    val parent: Parent,
    val properties: Map<String, Properties>,
    val children: List<Map<String, Child>>,
)


data class Parent(
    val type: String,
    val databaseId: String,
)

data class MultiSelect(
    val type: String = "multi_select",
    val multiSelect: List<MultiSelectOption>,
) : Properties {
    data class MultiSelectOption(
        val name: String,
        val color: String,
    )
}

data class Title(
    val type: String = "title",
    val title: List<TitleText>,
) : Properties {
    data class TitleText(
        val type: String = "text",
        val text: TextContent,
    ) {
        data class TextContent(
            val content: String?,
        )
    }
}

data class BulletedListItem(
    val richText: List<RichText>,
    val color: String = "default",
    val children: List<Map<String, Child>>? = null
) : Child

data class RichText(
    val text: Text,
) {
    data class Text(
        val content: String?,
    )
}

interface Child {

}

interface Properties {

}


