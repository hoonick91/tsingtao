package me.hoonick.tsingtao.notion.domain.port.out

import com.fasterxml.jackson.databind.JsonNode
import me.hoonick.tsingtao.notion.domain.ChildPage
import me.hoonick.tsingtao.notion.domain.Block
import me.hoonick.tsingtao.notion.domain.DailyContents
import me.hoonick.tsingtao.notion.domain.DailyPage

interface NotionPort {
    fun getChildrenBlocks(pageId: String): DailyPage
    fun getChildPagesInBlocks(pageId: String): List<ChildPage>
    fun getBlocks(pageId: String): JsonNode
    fun savePageInDatabase(databaseId: String, outstandingAllChildBlocks: List<Block>)
    fun saveDailyPage(contents: DailyContents)
}