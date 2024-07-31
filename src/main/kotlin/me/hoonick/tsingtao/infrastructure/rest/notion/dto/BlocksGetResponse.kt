package me.hoonick.tsingtao.infrastructure.rest.notion.dto

import com.fasterxml.jackson.annotation.JsonProperty
import me.hoonick.tsingtao.domain.ChildPage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class BlocksGetResponse(
    @JsonProperty("object") val `object`: String,
    val results: List<GetBlock>,
    val next_cursor: String?,
    val has_more: Boolean,
    val type: String,
    val block: BlockDetails,
    val request_id: String,
) {
    fun getChildPages(): List<ChildPage> {
        return this.results
            .filter { it.type == "child_page" }
            .sortedBy { it.created_time }
            .map { it.toChildPage() }
    }
}

data class GetBlock(
    @JsonProperty("object") val `object`: String,
    val id: String,
    val parent: GetParent,
    val created_time: String,
    val last_edited_time: String,
    val created_by: GetUser,
    val last_edited_by: GetUser,
    val has_children: Boolean,
    val archived: Boolean,
    val in_trash: Boolean,
    val type: String,
    val heading_2: GetHeading2? = null,
    val bulleted_list_item: BulletedListItem? = null,
    val child_page: GetChildPage? = null,
    val paragraph: GetParagraph? = null,
) {
    fun toChildPage(): ChildPage {
        return ChildPage(
            id = id,
            parentId = parent.page_id,
            createdAt = LocalDateTime.parse(created_time, DateTimeFormatter.ISO_DATE_TIME),
            title = child_page?.title
        )
    }
}

data class GetParent(
    val type: String,
    val page_id: String,
)

data class GetUser(
    @JsonProperty("object") val `object`: String,
    val id: String,
)

data class GetHeading2(
    val rich_text: List<RichText>,
    val is_toggleable: Boolean,
    val color: String,
)

data class BulletedListItem(
    val rich_text: List<RichText>,
    val color: String,
)

data class GetChildPage(
    val title: String,
)

data class RichText(
    val type: String,
    val text: GetText,
    val annotations: GetAnnotations,
    val plain_text: String,
    val href: String?,
)

data class GetText(
    val content: String,
    val link: String?,
)

data class GetAnnotations(
    val bold: Boolean,
    val italic: Boolean,
    val strikethrough: Boolean,
    val underline: Boolean,
    val code: Boolean,
    val color: String,
)

data class GetParagraph(
    val rich_text: List<RichText>,
    val color: String,
)

class BlockDetails // 필요한 경우 내용을 추가하십시오.
