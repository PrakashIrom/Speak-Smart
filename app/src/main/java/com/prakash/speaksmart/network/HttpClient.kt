package com.prakash.speaksmart.network

import com.prakash.speaksmart.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

val httpClient = HttpClient(OkHttp) {

    install(ContentNegotiation) {
        json(Json {
            encodeDefaults = true
            explicitNulls = false
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
        })
    }
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                android.util.Log.i(
                    "KtorNetwork",
                    message
                )
            }
        }
        level = LogLevel.ALL
    }
    defaultRequest {
        url(BuildConfig.OPENAI_BASE_URL)

        header(
            HttpHeaders.Authorization,
            "Bearer ${BuildConfig.OPENAI_API_KEY}"
        )

        contentType(ContentType.Application.Json)
    }
}