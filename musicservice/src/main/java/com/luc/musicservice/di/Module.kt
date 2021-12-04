package com.luc.musicservice.di

import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.luc.musicservice.DataSource
import com.luc.musicservice.MusicServiceConnection
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataSourceFactoryModule = module {
    factory {
        DefaultDataSourceFactory(
            androidContext(),
            Util.getUserAgent(androidContext(), "Red Hot Chili Peppers Application")
        )
    }

    factory {
        DataSource(get(), get())
    }

}

val exoplayerModule = module {
    factory {
        SimpleExoPlayer.Builder(androidContext()).build().apply {
            setAudioAttributes(audioAttributes, true)
            setHandleAudioBecomingNoisy(true)
        }
    }

    factory {
        AudioAttributes.Builder()
            .setContentType(C.CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build()
    }
}

val musicServiceConnectionModule = module {
    single { MusicServiceConnection(androidContext())}
}
val musicServiceModule = listOf(dataSourceFactoryModule, exoplayerModule, musicServiceConnectionModule)