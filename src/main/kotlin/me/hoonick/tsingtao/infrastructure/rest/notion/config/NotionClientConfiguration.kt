package me.hoonick.tsingtao.infrastructure.rest.notion.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import me.hoonick.tsingtao.infrastructure.rest.notion.service.NotionClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
class NotionClientConfiguration(
    @Value("\${notion.host}") val host: String,
    @Value("\${notion.token}") val token: String,
) {

    @Bean
    fun notionClient(jacksonObjectMapper: ObjectMapper): NotionClient {
        val objectMapper = jacksonObjectMapper().apply {
            setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
        }
        val adapter = RestClientAdapter.create(
            RestClient.builder()
                .baseUrl(host)
                .defaultHeaders { headers ->
                    headers.add("Content-Type", "application/json")
                    headers.add("Authorization", "Bearer $token")
                    headers.add("Notion-Version", "2022-06-28")
                }
                .messageConverters {
                    it.removeIf { it is MappingJackson2HttpMessageConverter }
                    it.add(MappingJackson2HttpMessageConverter(objectMapper))
                }
                .requestInterceptor(RestClientLoggingInterceptor())
                .build()
        )
        val factory = HttpServiceProxyFactory.builderFor(adapter).build()
        return factory.createClient(NotionClient::class.java)
    }
}