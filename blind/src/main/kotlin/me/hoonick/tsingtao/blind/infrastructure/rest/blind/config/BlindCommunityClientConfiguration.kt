package me.hoonick.tsingtao.blind.infrastructure.rest.blind.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import me.hoonick.tsingtao.blind.infrastructure.rest.blind.BlindClient
import me.hoonick.tsingtao.core.infrastructure.config.RestClientLoggingInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
class BlindCommunityClientConfiguration(
    @Value("\${blind.community-host}") val host: String,
) {
    @Bean
    fun blindClient(jacksonObjectMapper: ObjectMapper): BlindClient {
        val objectMapper = jacksonObjectMapper().apply {
            setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
        }
        val adapter = RestClientAdapter.create(
            RestClient.builder()
                .baseUrl(host)
                .defaultHeaders { headers ->
                    headers.add("Content-Type", "application/json")
                    headers.add(
                        "User-Agent",
                        "User-Agent Blinddate_renewal/2.9.7 (com.bada.blinddating; build:2.9.7; iOS 17.6.1) Alamofire/5.4.1"
                    )
                    headers.add(
                        "Cookie",
                        "connect.sid=s%3AqhVXO-Qng6rRh5y58UTISr9wGkhPxB3g.4in8d0ozR1uSBkywTvPttYY209Trfl9myGmFcrACavY; session=Fe26.2**71a2f6bdcd39b3a10d473b1b7a5a6ed077a00ed63573ec9a687b07b66cf63be9*Cd5s6iK549nIPXjo14AV1g*VruIY8h_HPxJm0G3BOja-e6ufoWKFAtQqYcEWgKyPhqZ_bK4YdI7jOmlxFvH-c6KpeyKM3A5msN0Y1JZLAv_-bwL9c5gRz5LXbbQGkavV3w**2e1250749aebc5509e4695a89fe458d8e096cab66cb8050fb82a27c2918ceca4*L2GbDDvCsm3ELtTYfoEd3qadgtUHVfAaVvz22n0gFJ8"
                    )
                    headers.add("Accept-Language", "ko-US;q=1.0, en-US;q=0.9")
                    headers.add("Accept-Encoding", "br;q=1.0, gzip;q=0.9, deflate;q=0.8")
                }
                .messageConverters {
                    it.removeIf { it is MappingJackson2HttpMessageConverter }
                    it.add(MappingJackson2HttpMessageConverter(objectMapper))
                }
                .requestInterceptor(RestClientLoggingInterceptor())
                .build()
        )
        val factory = HttpServiceProxyFactory.builderFor(adapter).build()
        return factory.createClient(BlindClient::class.java)
    }
}

