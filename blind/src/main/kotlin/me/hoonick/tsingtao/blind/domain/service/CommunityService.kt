package me.hoonick.tsingtao.blind.domain.service

import me.hoonick.tsingtao.blind.domain.BlindArticle
import me.hoonick.tsingtao.blind.infrastructure.rest.blind.service.BlindCommunityClientService
import org.springframework.stereotype.Service

@Service
class CommunityService(
    private val blindCommunityService: BlindCommunityClientService
) {

    fun getArticles(userId:String): List<BlindArticle> {
        return blindCommunityService.getArticles(userId)
    }
}