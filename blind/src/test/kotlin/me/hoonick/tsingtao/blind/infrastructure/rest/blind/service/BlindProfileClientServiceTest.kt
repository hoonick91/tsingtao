package me.hoonick.tsingtao.blind.infrastructure.rest.blind.service

import org.junit.jupiter.api.Disabled
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test

@Disabled
@SpringBootTest
class BlindProfileClientServiceTest @Autowired constructor(
    val blindProfileClientService: BlindProfileClientService
) {

    @Test
    fun getBlindProfile() {
        blindProfileClientService.getProfile("010901735061723220255718")
    }
}