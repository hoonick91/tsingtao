package me.hoonick.tsingtao.blind.domain

data class BlindArticle(
    val userId: String,
    val nickname: String,
    val gender: String,
    val category: String,
    val title: String,
    val content: String,
    val createdAt: String,
    val updatedAt: String
) {
}