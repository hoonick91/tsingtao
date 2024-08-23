package me.hoonick.me.hoonick.tsingtao.notion.infrastructure.rest

import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration
class RestClientConfiguration {
    fun defaultRestClientBuilder(properties: RestClientProperties): RestClient {
        return RestClient.builder()
            .baseUrl(properties.host)
            .build()
    }
}