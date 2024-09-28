package me.hoonick.tsingtao.notion.domain

import com.fasterxml.jackson.databind.JsonNode
import java.time.LocalDateTime

data class OldBlock(
    val id: String,
    val createdAt: LocalDateTime,
    val children: List<OldBlock>? = null,
    val type: String,
    val detail: Detail,
) {
}

data class Detail(
    val content: JsonNode,
) {
    val title: String = content.get("rich_text")
        .map { it.get("text").get("content").asText() }
        .first()
}
