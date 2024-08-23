package me.hoonick.me.hoonick.tsingtao.notion.infrastructure.rest.notion.dto

data class PageCreateResponse(
    val `object`: String,
    val id: String,
    val created_time: String,
    val last_edited_time: String,
    val created_by: User,
    val last_edited_by: User,
    val cover: Cover,
    val icon: Icon,
    val parent: Parent,
    val archived: Boolean,
    val in_trash: Boolean,
    val properties: Properties,
    val url: String,
    val public_url: String?,
    val developer_survey: String?,
    val request_id: String,
)

data class User(
    val `object`: String,
    val id: String,
)

data class Cover(
    val type: String? = null,
    val external: External,
)

data class External(
    val url: String,
)

data class Icon(
    val type: String? = null,
    val emoji: String,
)

data class Parent(
    val type: String? = null,
    val page_id: String,
)

data class Properties(
    val title: Title,
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
