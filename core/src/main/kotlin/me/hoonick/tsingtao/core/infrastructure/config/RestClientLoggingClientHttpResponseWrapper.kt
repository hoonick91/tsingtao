package me.hoonick.tsingtao.core.infrastructure.config

import org.springframework.http.client.ClientHttpResponse
import java.io.ByteArrayInputStream

class RestClientLoggingClientHttpResponseWrapper(
    private val response: ClientHttpResponse,
    private val responseBody: ByteArray,
) : ClientHttpResponse {
    override fun getHeaders() = response.headers
    override fun getBody() = ByteArrayInputStream(responseBody)
    override fun close() = response.close()
    override fun getStatusCode() = response.statusCode
    override fun getStatusText() = response.statusText
}