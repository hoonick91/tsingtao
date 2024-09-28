package me.hoonick.tsingtao.blind.infrastructure.rest.blind.service

import me.hoonick.tsingtao.blind.domain.BlindArticle
import me.hoonick.tsingtao.blind.infrastructure.rest.blind.BlindClient
import org.springframework.stereotype.Service
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Service
class BlindCommunityClientService(
    private val blindClient: BlindClient,
) {

    fun getArticles(userId: String): List<BlindArticle> {
        return blindClient.getArticlesBy(userId)
            .map {
                BlindArticle(
                    userId = it.user_id,
                    nickname = it.nickname,
                    gender = it.gender,
                    category = it.category,
                    title = it.title,
                    content = it.content,
                    createdAt = it.createdAt.toKST(),
                    updatedAt = it.updatedAt.toKST()
                )
            }
    }

    private fun String.toKST(): String {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val zonedDateTime = ZonedDateTime.parse(this, formatter)
        return zonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Seoul")).toString()
    }


}

