package com.prakash.speaksmart.domain.di

import com.prakash.speaksmart.domain.usecase.AnalyzeSpeechUseCase
import org.koin.dsl.module

val domainModule = module{
    single{
        AnalyzeSpeechUseCase(openAiRepo = get())
    }
}