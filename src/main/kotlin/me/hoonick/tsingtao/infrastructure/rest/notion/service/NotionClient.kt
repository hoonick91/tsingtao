package me.hoonick.tsingtao.infrastructure.rest.notion.service

import me.hoonick.tsingtao.infrastructure.rest.notion.dto.PageCreateRequest
import me.hoonick.tsingtao.infrastructure.rest.notion.dto.PageCreateResponse
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.service.annotation.PostExchange

/**
 * @see <a href="https://developers.notion.com/reference/patch-block-children">docs</a>
 * */
interface NotionClient {
    @PostExchange("/v1/pages")
    fun createPage(@RequestBody request: PageCreateRequest): PageCreateResponse

}
