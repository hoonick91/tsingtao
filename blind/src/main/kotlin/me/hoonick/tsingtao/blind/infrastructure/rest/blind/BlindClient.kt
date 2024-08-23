package me.hoonick.tsingtao.blind.infrastructure.rest.blind

import me.hoonick.tsingtao.blind.infrastructure.rest.blind.dto.ArticleResponse
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.service.annotation.GetExchange

interface BlindClient {

    @GetExchange("/board/myList/{userId}?page=1&pageSize=20")
    fun getArticlesBy(@PathVariable userId: String) : List<ArticleResponse>
}