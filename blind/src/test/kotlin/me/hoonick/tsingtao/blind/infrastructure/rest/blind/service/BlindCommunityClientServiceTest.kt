package me.hoonick.tsingtao.blind.infrastructure.rest.blind.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test

@SpringBootTest
class BlindCommunityClientServiceTest @Autowired constructor(
    private val blindCommunityClientService: BlindCommunityClientService,
) {

    @Test
    fun getArticles() {
    }


}