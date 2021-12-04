package com.luc.data.firestore

import com.luc.common.model.AlbumMetadata
import com.luc.common.model.SongMetadata
import com.luc.common.resource.NetworkStatus

interface FirestoreData {
    suspend fun getAlbums() : NetworkStatus<List<AlbumMetadata>>
    suspend fun getAllSongs() : NetworkStatus<List<SongMetadata>>
    suspend fun getSongsByAlbumName(albumName: String) : NetworkStatus<List<SongMetadata>>
}