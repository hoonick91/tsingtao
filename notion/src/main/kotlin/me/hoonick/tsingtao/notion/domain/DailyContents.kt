package me.hoonick.tsingtao.notion.domain

data class DailyContents(
    val contents: MutableMap<DailyPageHeading, List<Block>>,
) {

    fun removeSolvedQuestions(): List<Block> {
        val solvedQuestions = contents[DailyPageHeading.QUESTION]
            ?.filter { it.contents?.checked == true }
            ?: throw IllegalStateException()

        contents[DailyPageHeading.QUESTION] = contents[DailyPageHeading.QUESTION]
            ?.filter { it.contents?.checked != true }
            ?: throw IllegalStateException()

        return solvedQuestions
    }

    fun removeOutstanding(): List<Block> {
        val outstanding = contents[DailyPageHeading.OUTSTANDING]
            ?.filterNot { it.contents?.text == "ㅇㅇㅇ" }
            ?: throw IllegalStateException()
        contents[DailyPageHeading.OUTSTANDING] = emptyList()
        return outstanding
    }

    fun moveDone() {
        val done = contents[DailyPageHeading.TO_DO]
            ?.map { it.copy(contents = it.contents?.done()) }
            ?.onEach { it.contents?.addUpdatedAt() }
            ?.filterNot { it.contents?.childBlocks?.size == 0 }
            ?: emptyList()
        contents[DailyPageHeading.DONE] = (contents[DailyPageHeading.DONE] ?: listOf()) + done

        val todo = contents[DailyPageHeading.TO_DO]
            ?.filter { it.contents?.checked != true }
            ?.map { it.copy(contents = it.contents?.todo()) }
            ?.filterNot { it.type == BlockType.bulleted_list_item && it.contents?.childBlocks?.size == 0 }
            ?.toMutableList()
            ?: mutableListOf()

        contents[DailyPageHeading.TO_DO] = if (todo.isEmpty()) listOf(Block.emptyToDo()) else todo
    }

    fun moveBacklog() {
        val todo = contents[DailyPageHeading.TO_DO]
            ?.filter { it.contents?.checked != true }
            ?.map { it.copy(contents = it.contents?.todo()) }
            ?.onEach { it.contents?.addStar() }
            ?.filterNot { it.type == BlockType.bulleted_list_item && it.contents?.childBlocks?.size == 0 }
            ?.toMutableList()
            ?: mutableListOf()

        contents[DailyPageHeading.BACKLOG] = (contents[DailyPageHeading.BACKLOG] ?: listOf()) + todo

        val done = contents[DailyPageHeading.TO_DO]
            ?.map { it.copy(contents = it.contents?.done()) }
            ?.onEach { it.contents?.addUpdatedAt() }
            ?.filterNot { it.contents?.childBlocks?.size == 0 }
            ?: emptyList()

        contents[DailyPageHeading.TO_DO] = done.ifEmpty { listOf(Block.emptyToDo()) }
    }
}