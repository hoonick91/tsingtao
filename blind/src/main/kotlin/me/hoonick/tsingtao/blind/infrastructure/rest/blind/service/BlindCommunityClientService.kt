package me.hoonick.tsingtao.blind.infrastructure.rest.blind.service

import me.hoonick.tsingtao.blind.domain.BlindArticle
import me.hoonick.tsingtao.blind.domain.BlindReply
import me.hoonick.tsingtao.blind.domain.BoardType
import me.hoonick.tsingtao.blind.infrastructure.rest.blind.BlindClient
import org.springframework.stereotype.Service
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Service
class BlindCommunityClientService(
    private val blindClient: BlindClient,
) {

    fun getReplies(boardId: String) : List<BlindReply> {
        return blindClient.getReplies(boardId)
            .map { it.toBlindReply() }
    }

    fun getArticlesIn(boardType: BoardType): List<BlindArticle> {
        return blindClient.getArticles(boardType, 2, 20)
            .map {
                BlindArticle(
                    id = it.id.toString(),
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

    fun getArticles(userId: String): List<BlindArticle> {
        return blindClient.getArticlesBy(userId)
            .map {
                BlindArticle(
                    id = it.id.toString(),
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

