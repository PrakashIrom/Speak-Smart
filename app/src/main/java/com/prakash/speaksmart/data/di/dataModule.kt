package com.prakash.speaksmart.data.di

import com.prakash.speaksmart.data.remote.OpenAiApiService
import com.prakash.speaksmart.data.repository.OpenAiRepoImpl
import com.prakash.speaksmart.data.repository.SpeechRecognizerRepositoryImpl
import com.prakash.speaksmart.domain.repository.OpenAiRepo
import com.prakash.speaksmart.domain.repository.SpeechRecognizerRepository
import com.prakash.speaksmart.network.httpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single {
        OpenAiApiService(client = httpClient)
    }
    single<OpenAiRepo> {
        OpenAiRepoImpl(openAiApiService = get())
    }
    single<SpeechRecognizerRepository> {
        SpeechRecognizerRepositoryImpl(
            context = androidContext()
        )
    }
}