package me.hoonick.tsingtao.blind.domain

data class BlindArticle(
    val id: String,
    val userId: String,
    val nickname: String,
    val gender: String,
    val category: String,
    val title: String,
    val content: String,
    val createdAt: String,
    val updatedAt: String,
    var replies: List<BlindReply> = listOf(),
) {
    fun contains(targetUserId: String): Boolean {
        if (userId == targetUserId) return true
        return replies.count { it.userId == targetUserId } > 0
    }
}

data class BlindReply(
    val id: Int,
    val postId: Int,
    val groupId: Int,
    val replyId: Int,
    val userId: String,
    val nickname: String,
    val gender: String,
    val content: String,
    val likeCount: Int,
    val userType: Int,
    val createdAt: String?,
    val updatedAt: String?,
    val deletedAt: String?,
) {

}
