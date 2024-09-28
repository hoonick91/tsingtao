package me.hoonick.tsingtao.notion.application

import me.hoonick.tsingtao.notion.domain.service.NotionService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/notion")
class NoteController(
    private val notionService: NotionService,
) {

    @GetMapping("/page")
    fun createPage(@RequestParam apiKey: String) {
        if (apiKey != "daily") return
        notionService.createDailyPage()
    }
}