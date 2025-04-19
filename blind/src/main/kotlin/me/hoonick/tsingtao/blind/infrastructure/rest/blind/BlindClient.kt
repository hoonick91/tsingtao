package me.hoonick.tsingtao.blind.infrastructure.rest.blind

import me.hoonick.tsingtao.blind.domain.BoardType
import me.hoonick.tsingtao.blind.infrastructure.rest.blind.dto.ArticleResponse
import me.hoonick.tsingtao.blind.infrastructure.rest.blind.dto.ReplyResponse
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange

interface BlindClient {

    @GetExchange("/board/myList/{userId}?page=1&pageSize=20")
    fun getArticlesBy(@PathVariable userId: String): List<ArticleResponse>

    @GetExchange("/board/list_remove_ban/{boardType}")
    fun getArticles(
        @PathVariable boardType: BoardType,
        @RequestParam page: Int,
        @RequestParam pageSize: Int,
        @RequestParam userId: String = "010930947781712718015636",
    ) : List<ArticleResponse>

    @GetExchange("r")
    fun getReplies(
        @PathVariable boardId: String,
        @RequestParam userId: String = "010930947781712718015636",
    ) : List<ReplyResponse>
}