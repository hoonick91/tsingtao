package me.hoonick.tsingtao.notion.application

import me.hoonick.tsingtao.notion.domain.service.NotionService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class CreatingNoteProcessor(
    private val notionService: NotionService
) {

    @Scheduled(cron = "0 0 5 * * ?")
    fun process() {
        /**
         * 1. 오늘이 토요일이면, weekly 노트를 만든다.
         * 2. 오늘이 평일이면, daily 노트를 만든다.
         * */
        notionService.createDailyPage()
    }
}