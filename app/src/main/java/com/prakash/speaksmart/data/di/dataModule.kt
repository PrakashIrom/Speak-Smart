package com.prakash.speaksmart.data.di

import com.prakash.speaksmart.data.repository.SpeechRecognizerRepositoryImpl
import com.prakash.speaksmart.domain.repository.SpeechRecognizerRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single<SpeechRecognizerRepository> {
        SpeechRecognizerRepositoryImpl(
            context = androidContext()
        )
    }
}