package me.hoonick.tsingtao.notion

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication(scanBasePackages = ["me.hoonick.tsingtao"])
class TsingtaoApplication

fun main(args: Array<String>) {
    runApplication<TsingtaoApplication>(*args)
}
