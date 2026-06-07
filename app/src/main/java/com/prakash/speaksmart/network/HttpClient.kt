package com.prakash.speaksmart.network

import com.prakash.speaksmart.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

val httpClient = HttpClient(OkHttp) {
    install(ContentNegotiation) {
        json(Json {
            encodeDefaults = true
            explicitNulls = true
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
        })
    }
    defaultRequest {
        url{
            protocol = URLProtocol.HTTPS
            host = BuildConfig.OPENAI_BASE_URL
        }
        header(
            HttpHeaders.Authorization,
            "Bearer ${BuildConfig.OPENAI_API_KEY}"
        )

        contentType(ContentType.Application.Json)
    }
}