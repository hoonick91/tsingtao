package me.hoonick.tsingtao.notion.infrastructure.rest.notion.service

import com.fasterxml.jackson.databind.JsonNode
import me.hoonick.tsingtao.notion.infrastructure.rest.notion.dto.PageSaveRequest
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.PostExchange

/**
 * @see <a href="https://developers.notion.com/reference/patch-block-children">docs</a>
 * */
interface NotionClient {

    @PostExchange("/v1/pages")
    fun createPage2(@RequestBody request: PageCreateRequest2)

    @PostExchange("/v1/pages")
    fun savePage(@RequestBody request: PageSaveRequest): String

    @GetExchange("/v1/blocks/{page_id}/children")
    fun getBlocks(
        @PathVariable("page_id") pageId: String,
        @RequestParam("page_size") pageSize: Int = 100,
    ): JsonNode

}
