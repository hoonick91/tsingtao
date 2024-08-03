package me.hoonick.tsingtao.domain.port.out

import com.fasterxml.jackson.databind.JsonNode
import me.hoonick.tsingtao.domain.Block
import me.hoonick.tsingtao.domain.ChildPage

interface NotionPort {
    fun getChildPagesInBlocks(pageId: String): List<ChildPage>
    fun getBlocks(pageId: String, fieldName: String): List<Block>
    fun getBlocks(pageId: String): JsonNode
    fun createPage(blocks: Map<String, List<Block>>)
    fun updateBlock(doneBlockId: String, doneContent: List<Block>)
    fun deleteBlock(todoBlockId: String, doneContent: List<Block>)
}