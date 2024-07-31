package me.hoonick.tsingtao.domain.service

import me.hoonick.tsingtao.domain.port.out.NotionPort
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class NotionServiceTest @Autowired constructor(
    private val notionPort: NotionPort,
) {
    private val notionService = NotionService(notionPort)

    @Test
    fun test() {
        notionService.createDailyPage()
    }
}