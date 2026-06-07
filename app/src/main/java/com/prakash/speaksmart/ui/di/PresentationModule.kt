package com.prakash.speaksmart.ui.di

import com.prakash.speaksmart.ui.speech.SpeechViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module{
    viewModelOf(::SpeechViewModel)
}