package me.hoonick.tsingtao.blind

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["me.hoonick.tsingtao"])
class BlindApplication

fun main(args: Array<String>) {
    runApplication<BlindApplication>(*args)
}