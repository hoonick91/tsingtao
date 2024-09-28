package me.hoonick.tsingtao.notion.domain

enum class DailyPageHeading(
    val title: String,
    val maintainChildren: Boolean = false,
) {
    OUTSTANDING("💪-Outstanding"),
    BACKLOG("👋-Backlog", true),
    TO_DO("🟩-Todo", true),
    DONE("✅-Done", true),
    QUESTION("❓-Question", true),
    INFORMATION("💡-Information"),
    EVENT("🪩-Event");

    companion object {
        fun containsOf(text: String): DailyPageHeading {
            val title = text.split("-")
            return entries.firstOrNull { it.title.contains(title[1]) }
                ?: throw IllegalArgumentException("Invalid empty title")
        }
    }
}