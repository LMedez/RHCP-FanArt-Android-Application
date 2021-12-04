package com.luc.domain.di

import com.luc.domain.usecases.*
import org.koin.dsl.module

val useCaseModule = module {
    factory <GetAlbumsUseCase>{ GetAlbumsUseCaseImpl(musicDataRepository = get()) }
    factory <GetSongsUseCase>{ GetSongsUseCaseImpl(musicDataRepository = get()) }
    factory <AddSongsUseCase>{ AddSongsUseCaseImpl(musicDataRepository = get()) }
    factory <DeleteSongsUseCase>{ DeleteSongsUseCaseImpl(musicDataRepository = get()) }
}

val domainModule = listOf(useCaseModule)