package me.hoonick.tsingtao.blind.domain

data class BlindProfile(
    val phone: String,
    val nickname: String,
    val birthday: String,
    val job: String,
    val work: String,
    val school: String,
    val tall: Int,
    val smoke: Boolean,
    val lastLoginDate: String,
    val regDate: String,
    val getLikeCount: Int,
    val sendLikeCount: Int,
    val grade: String,
    val selfIntro: String,
    val address: String,
    val workAddress: String,
    val images: List<String>

) {
}