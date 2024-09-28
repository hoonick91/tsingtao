package me.hoonick.tsingtao.notion.domain.service

import me.hoonick.tsingtao.notion.domain.Block
import me.hoonick.tsingtao.notion.domain.DailyContents
import me.hoonick.tsingtao.notion.domain.DailyPageHeading
import me.hoonick.tsingtao.notion.domain.port.out.NotionPort
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

private const val DAILY_PAGE_ID = "e389c8ec9ddc40988a1e9d71c88b68bd"

@Service
class NotionService(
    private val notionPort: NotionPort,
) {
    fun createDailyPage() {
        val dailyPage = notionPort.getChildrenBlocks(DAILY_PAGE_ID)
        val recentDailyPage = dailyPage.getRecentChildDailyPage()
        val recentDailyPageDetail = notionPort.getChildrenBlocks(recentDailyPage.id)

        val dailyContents = DailyContents(
            contents = recentDailyPageDetail.getContents()
                .filter { it.contents != null }
                .filter { it.contents!!.text != null }
                .associateBy { DailyPageHeading.containsOf(it.contents!!.text!!) }
                .mapValues { getAllChildBlocks(it.value) }
                .toMutableMap()
        )

        dailyContents.removeOutstanding().saveTo(dailyPage.getChildDatabases("Outstanding").id)
        dailyContents.removeSolvedQuestions().saveTo(dailyPage.getChildDatabases("Solved questions").id)

        dailyContents.moveNotTodoToBackLog()
        dailyContents.moveDoingToDone()
        dailyContents.moveDoingToTodo()

        notionPort.saveDailyPage(dailyContents)
    }

    private fun List<Block>.saveTo(databaseId: String) {
        notionPort.savePageInDatabase(databaseId, this)
    }

    private fun getAllChildBlocks(block: Block): List<Block> {
        val childBlocks = notionPort.getChildrenBlocks(block.id).blocks
        childBlocks.forEach { it.addChildBlocks() }
        return childBlocks
    }

    private fun Block.addChildBlocks() {
        if (this.hasChildren) {
            val childBlocks = notionPort.getChildrenBlocks(this.id).blocks
            childBlocks.forEach { it.addChildBlocks() }
            this.addAll(childBlocks)
        }
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

    }

    private fun isNotWeekend() =
        LocalDate.now().dayOfWeek != DayOfWeek.SATURDAY || LocalDate.now().dayOfWeek != DayOfWeek.SUNDAY

    private fun isDailyPage(title: String?): Boolean {
        if (title == null) return false
        return kotlin.runCatching { title.split("(")[0].toLocalDate() }.isSuccess
    }

    private fun isDateInCurrentWeek(date: LocalDate): Boolean {
        val today = LocalDate.now()
        val startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

        return date.isEqual(startOfWeek) || date.isEqual(endOfWeek) || (date.isAfter(startOfWeek) && date.isBefore(
            endOfWeek
        ))
    }


}

private fun String.toLocalDate(): LocalDate {
    return LocalDate.parse(this)
}

