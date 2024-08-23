package me.hoonick.tsingtao.notion.notion.domain.service

import me.hoonick.tsingtao.notion.domain.port.out.NotionPort
import me.hoonick.tsingtao.notion.domain.service.NotionService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class NotionServiceTest @Autowired constructor(
    private val notionPort: NotionPort,
) {
    private val notionService = NotionService(notionPort)

    @Test
    fun daily() {
        notionService.createDailyPage()
    }

    @Test
    fun weekly() {
        notionService.createWeeklyPage()
    }
}