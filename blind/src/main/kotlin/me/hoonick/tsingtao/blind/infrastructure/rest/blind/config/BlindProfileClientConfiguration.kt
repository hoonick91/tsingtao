package me.hoonick.tsingtao.blind.infrastructure.rest.blind.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import me.hoonick.tsingtao.blind.infrastructure.rest.blind.BlindMatchClient
import me.hoonick.tsingtao.blind.infrastructure.rest.blind.BlindProfileClient
import me.hoonick.tsingtao.core.infrastructure.config.RestClientLoggingInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory


@Configuration
class BlindProfileClientConfiguration(
    @Value("\${blind.profile-host}") val host: String
) {

    @Bean
    fun blindProfileClient(jacksonObjectMapper: ObjectMapper): BlindProfileClient {
        val objectMapper = jacksonObjectMapper().apply {
            setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
        }
        val adapter = RestClientAdapter.create(
            RestClient.builder()
                .baseUrl(host)
                .defaultHeaders { headers ->
                    headers.add("Content-Type", "application/json; charset=utf-8")
//                    headers.add(
//                        "User-Agent",
//                        "User-Agent Blinddate_renewal/2.9.7 (com.bada.blinddating; build:2.9.7; iOS 17.6.1) Alamofire/5.4.1"
//                    )
//                    headers.add("Accept-Language", "ko-US;q=1.0, en-US;q=0.9")
//                    headers.add("Accept-Encoding", "br;q=1.0, gzip;q=0.9, deflate;q=0.8")
                }
                .messageConverters {
                    it.removeIf { it is MappingJackson2HttpMessageConverter }
                    it.add(MappingJackson2HttpMessageConverter(objectMapper))
                }
                .requestInterceptor(RestClientLoggingInterceptor())
                .build()
        )
        val factory = HttpServiceProxyFactory.builderFor(adapter).build()
        return factory.createClient(BlindProfileClient::class.java)
    }

}