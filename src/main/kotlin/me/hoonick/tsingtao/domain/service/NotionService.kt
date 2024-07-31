package me.hoonick.tsingtao.domain.service

import com.fasterxml.jackson.databind.JsonNode
import me.hoonick.tsingtao.domain.Block
import me.hoonick.tsingtao.domain.Detail
import me.hoonick.tsingtao.domain.port.out.NotionPort
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val DAILY_PAGE_ID = "c63366bc-abdd-47b0-b463-db86acff3e49"

class NotionService(
    private val notionPort: NotionPort,
) {
    fun createDailyPage() {
        val recentPageId = notionPort.getChildPagesInBlocks(DAILY_PAGE_ID).last().id
        val heading2Blocks = notionPort.getBlocks(recentPageId, "heading_2")

        val todoContent = getContent(heading2Blocks, "Todo")
        val questionContent = getContent(heading2Blocks, "Question")

        notionPort.createPage(
            mapOf("Todo" to todoContent, "Question" to questionContent)
        )
    }

    private fun getContent(heading2Blocks: List<Block>, fieldName: String): List<Block> {
        val id = heading2Blocks.filter(fieldName).first().id
        return notionPort.getBlocks(id)
            .findBy("to_do")
            .filterUnchecked()
    }


}

private fun JsonNode.findBy(fieldName: String): List<Block> {
    return this.get("results")
        .map {
            Block(
                id = it.get("id").asText(),
                createdAt = it.get("created_time").toLocalDateTime(),
                hasChildren = it.get("has_children").asBoolean(),
                type = it.get("type").asText(),
                detail = Detail(it.get(fieldName))
            )
        }

}

private fun List<Block>.filter(fieldName: String): List<Block> {
    return this.filter { it.detail.title.contains(fieldName) }
}

private fun List<Block>.filterUnchecked(): List<Block> {
    println()
    return this
        .filter {
            !it.detail.content
                .get("checked")
                .asBoolean()
        }
}

private fun JsonNode.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.parse(this.asText(), DateTimeFormatter.ISO_DATE_TIME)
}

