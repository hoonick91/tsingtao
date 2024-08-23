package me.hoonick.me.hoonick.tsingtao.notion.domain

import java.time.LocalDateTime

data class ChildPage(
    val id: String,
    val parentId: String,
    val createdAt: LocalDateTime,
    val title: String?,
) {
}