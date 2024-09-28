package me.hoonick.tsingtao.blind.domain.service

import me.hoonick.tsingtao.blind.domain.BlindProfile
import me.hoonick.tsingtao.blind.infrastructure.rest.blind.service.BlindProfileClientService
import org.springframework.stereotype.Service

@Service
class ProfileService(
    private val blindProfileClientService: BlindProfileClientService,
) {
    fun getProfile(targetId: String): BlindProfile {
        return blindProfileClientService.getProfile(targetId)
//            .takeIf { it.getLikeCount > 0 }
//            .also {
//                val likedProfiles = blindProfileClientService.getLikedProfiles(targetId)
//                it?.addLikedProfiles(likedProfiles)
//            } ?: throw IllegalStateException("")
    }
}