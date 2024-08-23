package me.hoonick.me.hoonick.tsingtao.notion.infrastructure.rest

import org.jetbrains.annotations.NotNull

abstract class RestClientProperties(
    @field:NotNull open val host: String,
) {

}