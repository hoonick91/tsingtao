package me.hoonick.tsingtao.notion.notion.infrastructure.rest.notion.service

import me.hoonick.tsingtao.notion.infrastructure.rest.notion.service.NotionClientService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class NotionClientServiceTest @Autowired constructor(
    private val notionClientService: NotionClientService,
) {


}