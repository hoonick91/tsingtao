package me.hoonick.tsingtao.notion.domain

import java.time.LocalDate
import java.time.LocalDateTime


data class DailyPage(
    val blocks: List<Block>,
) {

    fun getChildDatabases(title: String): Block {
        return blocks
            .filter { it.type == BlockType.child_database }
            .firstOrNull { it.contents?.text == title }
            ?: throw IllegalStateException("$title database not exist.")
    }


    fun getRecentChildDailyPage(): Block {
        return blocks
            .filter { it.type == BlockType.child_page }
            .maxByOrNull { it.contents?.text!! }!!
    }

    fun getContents(text: String): Block {
        return this.blocks
            .filter { it.contents != null }
            .filter { it.contents!!.text != null }
            .firstOrNull { it.contents!!.text!!.contains(text) }
            ?: throw IllegalStateException("No contents")
    }

    fun getContents(): List<Block> {
        return this.blocks
    }
}

data class Block(
    val id: String,
    val parentId: String,
    val createdAt: LocalDateTime,
    val hasChildren: Boolean,
    val type: BlockType,
    val contents: Contents?,
) {
    fun addAll(blocks: List<Block>) {
        this.contents?.addAll(blocks)
    }

    companion object {
        fun emptyToDo(): Block {
            return Block(
                id = "",
                parentId = "",
                createdAt = LocalDateTime.now(),
                hasChildren = false,
                type = BlockType.to_do,
                contents = Contents(
                    type = ContentsType.text,
                    text = "ㅇㅇㅇ",
                    checked = false
                )
            )
        }
    }

}

data class Contents(
    val type: ContentsType? = null,
    var text: String? = null,
    val checked: Boolean? = null,
    val childBlocks: MutableList<Block>? = mutableListOf(),
) {
    fun addAll(blocks: List<Block>) {
        this.childBlocks?.addAll(blocks)
    }

    fun addUpdatedAt() {
        this.text += " #${LocalDate.now()}"
    }

    fun done(): Contents {
        return this.copy(
            childBlocks = this.childBlocks?.filter { it.contents?.checked == true }?.toMutableList()
        )
    }

    fun todo(): Contents {
        return this.copy(
            childBlocks = this.childBlocks?.filter { it.contents?.checked != true }?.toMutableList()
        )
    }

    fun addStar() {
        this.text += "⭐"
    }

}

enum class ContentsType {
    text,

}

enum class BlockType {
    paragraph, child_database, child_page, heading_2, to_do, bulleted_list_item, column_list, toggle
}
