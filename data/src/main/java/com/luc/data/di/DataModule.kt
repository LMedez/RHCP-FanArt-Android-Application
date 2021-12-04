package com.luc.data.di

import android.app.Application
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.luc.data.firestore.FirestoreDataImpl
import com.luc.data.firestore.FirestoreData
import com.luc.data.local.LocalMusicDAO
import com.luc.data.local.LocalDatabase
import com.luc.data.local.LocalMusicSource
import com.luc.data.repository.MusicDataRepositoryImpl
import com.luc.domain.repository.MusicDataRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val firebaseModule = module {
    single { FirebaseFirestore.getInstance() }
}

val roomModule = module {
    fun provideDatabase(application: Application): LocalDatabase {
        return Room.databaseBuilder(application, LocalDatabase::class.java, "music-data")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideCountriesDao(database: LocalDatabase): LocalMusicDAO {
        return database.musicDao()
    }

    single { provideDatabase(androidApplication()) }
    single { provideCountriesDao(get()) }
}

val repositoryModule = module {
    factory<FirestoreData> {
        FirestoreDataImpl(
            firestore = get(),
        )
    }
    factory { LocalMusicSource(get()) }
    factory<MusicDataRepository> { MusicDataRepositoryImpl(firestoreData = get(), get()) }
}

val dataModule = listOf(repositoryModule, firebaseModule, roomModule)