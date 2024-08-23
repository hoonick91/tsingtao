package me.hoonick.tsingtao.blind.infrastructure.rest.blind.dto

data class ArticleResponse(
    val id: Int,
    val title: String,
    val user_id: String,
    val nickname: String,
    val gender: String,
    val category: String,
    val content: String,
    val img: String,
    val count: Int,
    val thumbup: Int,
    val thumbdown: Int,
    val notice: Int,
    val hot: Int,
    val hot_selected_at: String?, // Nullable because it can be null
    val deletedby: String,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?, // Nullable because it can be null
    val post_reply_count: Int,
    val user_type: Int
)
