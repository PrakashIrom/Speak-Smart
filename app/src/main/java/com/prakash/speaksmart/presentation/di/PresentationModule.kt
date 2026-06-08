package com.prakash.speaksmart.presentation.di

import com.prakash.speaksmart.presentation.ui.SpeechViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module{
    viewModelOf(::SpeechViewModel)
}