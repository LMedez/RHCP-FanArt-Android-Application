package com.luc.data.repository

import com.luc.common.entities.*
import com.luc.common.model.AlbumMetadata
import com.luc.common.model.SongMetadata
import com.luc.common.resource.NetworkStatus
import com.luc.data.firestore.FirestoreData
import com.luc.data.local.LocalMusicSource
import com.luc.domain.repository.MusicDataRepository
import java.time.format.ResolverStyle
import java.util.*

class MusicDataRepositoryImpl(
    private val firestoreData: FirestoreData,
    private val localData: LocalMusicSource,
) : MusicDataRepository {

    override fun getPlayList() = localData.getPlayList()

    override fun getSongsByQuery(query: String) = localData.getSongsByQuery(query)

    override suspend fun insertLastSongPlayed(songMetadata: SongMetadata) {
        localData.insertLastSongPlayed(
            LastSongPlayedEntity(
                songMetadata.id,
                songMetadata.asSongMetadataEntity()
            )
        )
    }

    override fun getLastSongsPlayed() =
        localData.getLastSongsPlayed()

    override suspend fun addPlayList(
        playlistEntity: PlaylistEntity,
        playlistSongEntity: List<SongMetadataEntity>,
    ) {
        localData.addPlayList(playlistEntity, playlistSongEntity)
    }

    override suspend fun getFavSong() = localData.getFavSongs()
    override suspend fun getFavAlbums() = localData.getFavAlbums()

    override suspend fun deleteFavSong(songMetadata: SongMetadata) =
        localData.deleteFavSong(songMetadata)

    override suspend fun deleteFavAlbum(albumMetadata: AlbumMetadata) =
        localData.deleteFavAlbum(albumMetadata)

    override suspend fun addFavSong(songMetadata: SongMetadata) {
        try {
            localData.insertFavSong(songMetadata)
        } catch (e: Exception) {
            // TODO(add exceptions)
        }
    }

    override suspend fun addFavAlbum(albumMetadata: AlbumMetadata) {
        try {
            localData.insertFavAlbum(albumMetadata)
        } catch (e: Exception) {
            // TODO(add exceptions)
        }
    }

    override suspend fun getAlbums(): NetworkStatus<List<AlbumMetadata>> {
        return try {
            val albumsInLocal = localData.getAllAlbums()
            if (albumsInLocal.isEmpty()) {
                val firestoreData = firestoreData.getAlbums()
                if (firestoreData is NetworkStatus.Success) {
                    localData.insertAlbum(firestoreData.data)
                }
                firestoreData
            } else NetworkStatus.Success(albumsInLocal)
        } catch (e: Exception) {
            NetworkStatus.Error(null, "An error when getting data")
        }
    }

    override suspend fun getAllSongs(): NetworkStatus<List<SongMetadata>> {
        return try {
            val songsInLocal = localData.getAllSongs()
            if (songsInLocal.isEmpty()) {
                val firestoreData = firestoreData.getAllSongs()
                if (firestoreData is NetworkStatus.Success) {
                    localData.insertSongs(firestoreData.data)
                }
                firestoreData
            } else NetworkStatus.Success(songsInLocal)
        } catch (e: Exception) {
            NetworkStatus.Error(e, "An error when getting data")
        }

    }

    override suspend fun getSongs(limit: Int): NetworkStatus<List<SongMetadata>> {
        return when(val songs = getAllSongs()) {
            is NetworkStatus.Success -> {
                NetworkStatus.Success(songs.data.take(limit))
            }
            else -> songs
        }
    }

    override suspend fun getRandomSongs(): NetworkStatus<List<SongMetadata>> {
        return when (val songs = getAllSongs()) {
            is NetworkStatus.Success -> NetworkStatus.Success(songs.data.shuffled().take(15))
            else -> songs
        }
    }

    override suspend fun getSongsByAlbumName(albumName: String): NetworkStatus<List<SongMetadata>> {
        return try {
            val songsInLocal = localData.getSongsByAlbumName(albumName.lowercase(Locale.getDefault()))
            if (songsInLocal.isEmpty()) {
                val firestoreData = firestoreData.getSongsByAlbumName(albumName)
                if (firestoreData is NetworkStatus.Success) {
                    localData.insertSongs(firestoreData.data)
                }
                firestoreData
            } else NetworkStatus.Success(songsInLocal)
        } catch (e: Exception) {
            NetworkStatus.Error(null, "An error when getting data")
        }
    }
}