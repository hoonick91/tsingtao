package me.hoonick.tsingtao.core.infrastructure.config

import org.jetbrains.annotations.NotNull

abstract class RestClientProperties(
    @field:NotNull open val host: String,
) {

}