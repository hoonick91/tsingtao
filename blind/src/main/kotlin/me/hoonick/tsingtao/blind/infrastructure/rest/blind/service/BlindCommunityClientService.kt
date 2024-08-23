package me.hoonick.tsingtao.blind.infrastructure.rest.blind.service

import me.hoonick.tsingtao.blind.domain.BlindArticle
import me.hoonick.tsingtao.blind.infrastructure.rest.blind.BlindClient
import org.springframework.stereotype.Service

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
                    createdAt = it.createdAt,
                    updatedAt = it.updatedAt
                )
            }
    }


}