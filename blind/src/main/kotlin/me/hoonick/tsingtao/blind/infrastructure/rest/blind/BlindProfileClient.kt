package me.hoonick.tsingtao.blind.infrastructure.rest.blind

import me.hoonick.tsingtao.blind.infrastructure.rest.blind.dto.BlindProfileRequest
import me.hoonick.tsingtao.blind.infrastructure.rest.blind.dto.BlindProfileResponse
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.service.annotation.PostExchange

interface BlindProfileClient {
    @PostExchange("/api/user/profile")
    fun getProfile(@RequestBody request: BlindProfileRequest) : BlindProfileResponse
    
    fun getLikedProfiles(targetId: String)
}