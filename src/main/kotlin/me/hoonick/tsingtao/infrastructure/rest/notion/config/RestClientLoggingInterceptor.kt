package me.hoonick.tsingtao.infrastructure.rest.notion.config

import mu.KLogger
import mu.KotlinLogging
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import java.nio.charset.StandardCharsets.UTF_8

class RestClientLoggingInterceptor : ClientHttpRequestInterceptor {
    companion object {
        private val log: KLogger = KotlinLogging.logger {}
    }

    override fun intercept(
        request: HttpRequest,
        body: ByteArray,
        execution: ClientHttpRequestExecution,
    ): ClientHttpResponse {

//        val requestBody = String(body, StandardCharsets.UTF_8)
        val response = execution.execute(request, body)
        val responseBody = response.body.buffered().use { it.readBytes() }

        log.info {
            """

                |================================request begin================================
                |URI    : ${request.uri}
                |Method : ${request.method}
                |Headers: ${request.headers}
                |Body   : ${String(body, UTF_8)}
                |Status : ${response.statusCode}
                |================================request end===================================
            """.trimIndent()
        }


        return RestClientLoggingClientHttpResponseWrapper(response, responseBody)
    }

}
