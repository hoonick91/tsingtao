package me.hoonick.tsingtao.core.infrastructure.config

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