package me.hoonick.tsingtao.notion.domain.port.out

import com.fasterxml.jackson.databind.JsonNode
import me.hoonick.me.hoonick.tsingtao.notion.domain.ChildPage
import me.hoonick.tsingtao.notion.domain.*

interface NotionPort {
    fun getChildrenBlocks(pageId: String): DailyPage
    fun getChildPagesInBlocks(pageId: String): List<ChildPage>
    fun getBlocks(pageId: String, fieldName: String): List<OldBlock>
    fun getBlocks(pageId: String): JsonNode
    fun createPage(blocks: Map<String, List<OldBlock>>)
    fun updateBlock(doneBlockId: String, doneContent: List<OldBlock>)
    fun deleteBlock(todoBlockId: String, doneContent: List<OldBlock>)
    fun savePageInDatabase(databaseId: String, outstandingAllChildBlocks: List<Block>)
    fun saveDailyPage(contents: DailyContents)
}