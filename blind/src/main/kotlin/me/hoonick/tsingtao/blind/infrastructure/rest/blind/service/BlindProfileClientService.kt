package me.hoonick.tsingtao.blind.infrastructure.rest.blind.service

import me.hoonick.tsingtao.blind.domain.BlindProfile
import me.hoonick.tsingtao.blind.infrastructure.rest.blind.BlindProfileClient
import me.hoonick.tsingtao.blind.infrastructure.rest.blind.dto.BlindProfileRequest
import org.springframework.stereotype.Service

@Service
class BlindProfileClientService(
    private val blindProfileClient: BlindProfileClient
) {

    fun getProfile(targetId: String) : BlindProfile {
        val request = BlindProfileRequest(targetId)
         return blindProfileClient.getProfile(request).toBlindProfile()
    }

    fun getLikedProfiles(targetId: String): Any {
        return blindProfileClient.getLikedProfiles(targetId)
        TODO("Not yet implemented")
    }
}