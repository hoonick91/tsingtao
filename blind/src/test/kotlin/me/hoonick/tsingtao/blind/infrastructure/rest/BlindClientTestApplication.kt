package me.hoonick.tsingtao.blind.infrastructure.rest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["me.hoonick.tsingtao"])
class BlindClientTestApplication

fun main(args: Array<String>) {
    runApplication<BlindClientTestApplication>(*args)
}