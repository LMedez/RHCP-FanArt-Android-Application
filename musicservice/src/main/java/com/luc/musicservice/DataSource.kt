package com.luc.musicservice

import android.support.v4.media.MediaMetadataCompat
import android.util.Log
import com.luc.common.Constants.ALL_MUSIC
import com.luc.common.Constants.EXPLORE_MUSIC
import com.luc.common.Constants.FAVORITE_MUSIC
import com.luc.common.entities.asSongMetadata
import com.luc.common.model.SongMetadata
import com.luc.common.resource.NetworkStatus
import com.luc.domain.usecases.AddSongsUseCase
import com.luc.domain.usecases.GetSongsUseCase
import com.luc.musicservice.extensions.asSongMetadataMedia
import com.luc.musicservice.extensions.from
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class DataSource(
    private val getSongsUseCase: GetSongsUseCase,
    private val addSongsUseCase: AddSongsUseCase,
) : Iterable<MediaMetadataCompat> {

    var musicData = emptyList<MediaMetadataCompat>()

    override fun iterator() = musicData.iterator()

    private val onReadyListeners = mutableListOf<(Boolean) -> Unit>()

//    suspend fun getSongs() = withContext(Dispatchers.IO) {
//        state = State.STATE_INITIALIZING
//        val musicMetadata = when (val songData = getSongsUseCase.getSongs()) {
//            is NetworkStatus.Success -> songData.data
//            is NetworkStatus.Error -> {
//                state = State.STATE_ERROR
//                return@withContext
//            }
//            is NetworkStatus.Loading -> emptyList()
//        }
//
//        val musicCompat = musicMetadata.map { songMetadata ->
//            MediaMetadataCompat.Builder()
//                .from(songMetadata)
//                .build()
//        }.toList()
//
//        // Add description keys to be used by the ExoPlayer MediaSession extension when
//        // announcing metadata changes.
//        musicCompat.forEach { it.description.extras?.putAll(it.bundle) }
//        musicData = musicCompat
//        state = State.STATE_INITIALIZED
//        // state is re assigned as initializing for get new data after
//        state = State.STATE_INITIALIZING
//
//    }

    suspend fun getSongs(parentId: String) = withContext(Dispatchers.IO) {
        state = State.STATE_INITIALIZING
        val musicMetadata: List<SongMetadata> = when (parentId) {
            EXPLORE_MUSIC -> getSongsMetadata(getSongsUseCase.getSongs())
            ALL_MUSIC -> getSongsMetadata(getSongsUseCase.getAllSongs())
            FAVORITE_MUSIC -> getSongsUseCase.getFavSongs().first().asSongMetadata()
            else -> getSongsMetadata(getSongsUseCase.getSongsByAlbumName(parentId))
        }

        val musicCompat = musicMetadata.map { songMetadata ->
            MediaMetadataCompat.Builder()
                .from(songMetadata)
                .build()
        }.toList()

        // Add description keys to be used by the ExoPlayer MediaSession extension when
        // announcing metadata changes.
        musicCompat.forEach { it.description.extras?.putAll(it.bundle) }
        musicData = musicCompat
        state = State.STATE_INITIALIZED

        // state is re assigned as initializing for get new data after
        state = State.STATE_INITIALIZING

    }

    suspend fun saveRecentSongPlayed(mediaMetadataCompat: MediaMetadataCompat?) {
        addSongsUseCase.addLastSongPlayed(mediaMetadataCompat?.asSongMetadataMedia() ?: return)
    }

    private fun getSongsMetadata(listOfSongs: NetworkStatus<List<SongMetadata>>): List<SongMetadata> {
        val musicMetadata = when (listOfSongs) {
            is NetworkStatus.Success -> listOfSongs.data
            is NetworkStatus.Error -> {
                state = State.STATE_ERROR
                return emptyList()
            }
            is NetworkStatus.Loading -> emptyList()
        }

        return musicMetadata

    }

    var state: State = State.STATE_CREATED
        set(value) {
            if (value == State.STATE_INITIALIZED || value == State.STATE_ERROR) {
                synchronized(onReadyListeners) {
                    field = value
                    onReadyListeners.forEach { listener ->
                        listener(state == State.STATE_INITIALIZED)
                    }
                    onReadyListeners.clear()
                }
            } else {
                field = value
            }
        }

    fun whenReady(performAction: (Boolean) -> Unit): Boolean {
        return if (state == State.STATE_CREATED || state == State.STATE_INITIALIZING) {
            onReadyListeners += performAction
            false
        } else {
            performAction(state != State.STATE_ERROR)
            true
        }
    }

    enum class State {
        STATE_CREATED,
        STATE_INITIALIZING,
        STATE_INITIALIZED,
        STATE_ERROR,
    }
}