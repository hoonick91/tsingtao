package me.hoonick.tsingtao.domain

import com.fasterxml.jackson.databind.JsonNode
import java.time.LocalDateTime

data class Block(
    val id: String,
    val createdAt: LocalDateTime,
    val hasChildren: Boolean,
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
