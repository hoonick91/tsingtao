package me.hoonick.tsingtao.blind.application.controller

import me.hoonick.tsingtao.blind.domain.service.CommunityService
import me.hoonick.tsingtao.blind.domain.service.ProfileService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class ProfileController(
    private val profileService: ProfileService,
    private val communityService: CommunityService
) {

    @GetMapping("/blind/profile/{userId}")
    fun getProfile(@PathVariable userId: String, model: Model): String {
        val profile = profileService.getProfile(userId)
        val articles = communityService.getArticles(userId)
        model.addAttribute("profile", profile)
        model.addAttribute("articles", articles)
        return "profile"
    }
}