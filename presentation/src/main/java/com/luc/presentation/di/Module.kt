package com.luc.presentation.di

import com.luc.presentation.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module


val presentationModule = module {
    viewModel { HomeFragmentViewModel() }
    viewModel { MainActivityViewModel(get()) }
    viewModel { MusicPlayerViewModel(get()) }
    viewModel { AlbumDetailViewModel(get()) }
    viewModel { MediaPlayerDetailFragmentViewModel() }
    viewModel { AlbumsViewModel(get()) }
    viewModel { UserMusicDataViewModel(get(), get(), get(), get()) }
    viewModel { MusicDataViewModel(get(), get()) }
}
