package me.hoonick.tsingtao.notion.domain.service

import com.fasterxml.jackson.databind.JsonNode
import me.hoonick.me.hoonick.tsingtao.notion.domain.Block
import me.hoonick.me.hoonick.tsingtao.notion.domain.Detail
import me.hoonick.tsingtao.notion.domain.port.out.NotionPort
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

private const val DAILY_PAGE_ID = "c63366bc-abdd-47b0-b463-db86acff3e49"

@Service
class NotionService(
    private val notionPort: NotionPort,
) {
    fun createDailyPage() {

        /**
         * 1. daily page
         * 2. recent date
         * 3. -> todo, question,
         * 4. todo 끝까지 조회,
         * 5. question 끝까지 조회
         * */

        val heading2Blocks = getRecentDailyHeading2Blocks()
        heading2Blocks.move("Todo", "Done")

        val todoContent = getContent(heading2Blocks, "Todo").filterUnchecked()
        val questionContent = getContent(heading2Blocks, "Question").filterUnchecked()

        notionPort.createPage(
            mapOf(
                "Todo" to todoContent,
                "Question" to questionContent
            )
        )
    }

    fun createWeeklyPage() {
        if (isNotWeekend()) {
            return
        }
        /**
         * Todo를 하나로 합쳐준다.
         * Done을 하나로 합쳐준다.
         * question을 하나로 합쳐 준다.
         * 백로그들을 하나로 합쳐준다.
         * 이벤트...?
         * */
        val dailyPages = notionPort.getChildPagesInBlocks(DAILY_PAGE_ID)
            .filter { isDailyPage(it.title) }
            .filter { isDateInCurrentWeek(it.createdAt.plusHours(9).toLocalDate()) }
            .map { notionPort.getChildPagesInBlocks(it.id) }

        println()
    }

    private fun getContent(heading2Blocks: List<Block>, fieldName: String): List<Block> {
        val id = heading2Blocks.filter(fieldName).first().id
        return getContentBy(id)
    }

    private fun getContentBy(id: String): List<Block> {
        return notionPort.getBlocks(id)
            .get("results")
            .map {
                Block(
                    id = it.get("id").asText(),
                    createdAt = it.get("created_time").toLocalDateTime(),
                    children = if (it.get("has_children").asBoolean()) getContentBy(
                        it.get("id").asText()
                    ) else null,
                    type = it.get("type").asText(),
                    detail = Detail(it.get(it.get("type").asText())),
                )
            }
    }

    private fun isNotWeekend() =
        LocalDate.now().dayOfWeek != DayOfWeek.SATURDAY || LocalDate.now().dayOfWeek != DayOfWeek.SUNDAY

    private fun isDailyPage(title: String?): Boolean {
        if (title == null) return false
        return kotlin.runCatching { title.split("(")[0].toLocalDate() }.isSuccess
    }

    private fun getRecentDailyHeading2Blocks(): List<Block> {
        val recentPageId = notionPort.getChildPagesInBlocks(DAILY_PAGE_ID).last().id
        val heading2Blocks = notionPort.getBlocks(recentPageId, "heading_2")
        return heading2Blocks
    }

    private fun isDateInCurrentWeek(date: LocalDate): Boolean {
        val today = LocalDate.now()
        val startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

        return date.isEqual(startOfWeek) || date.isEqual(endOfWeek) || (date.isAfter(startOfWeek) && date.isBefore(
            endOfWeek
        ))
    }

    private fun List<Block>.move(from: String, to: String) {
        val todoBlockId = this.filter(from).first().id
        val doneBlockId = this.filter(to).first().id
        val doneContent = getContent(this, from).filterChecked()
        notionPort.updateBlock(doneBlockId, doneContent)
        notionPort.deleteBlock(todoBlockId, doneContent)
    }


}


private fun String.toLocalDate(): LocalDate {
    return LocalDate.parse(this)
}

private fun List<Block>.filter(fieldName: String): List<Block> {
    return this.filter { it.detail.title.contains(fieldName) }
}

private fun List<Block>.filterUnchecked(): List<Block> {
    return this
        .filter {
            val checked = it.detail.content
                .get("checked")
            if (checked != null) {
                return@filter !checked.asBoolean()
            }
            return@filter false
        }
}

private fun List<Block>.filterChecked(): List<Block> {
    println()
    return this
        .filter {
            it.detail.content
                .get("checked")
                .asBoolean()
        }
}

private fun JsonNode.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.parse(this.asText(), DateTimeFormatter.ISO_DATE_TIME)
}

