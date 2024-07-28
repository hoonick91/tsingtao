package me.hoonick.tsingtao.infrastructure.rest

import org.jetbrains.annotations.NotNull

abstract class RestClientProperties(
    @field:NotNull open val host: String,
) {

}