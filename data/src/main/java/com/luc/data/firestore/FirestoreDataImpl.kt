package com.luc.data.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.luc.common.model.AlbumMetadata
import com.luc.common.model.SongMetadata
import com.luc.common.resource.NetworkStatus
import com.luc.data.firestore.Constants.ALBUM_COLLECTION_NAME
import com.luc.data.firestore.Constants.SONG_COLLECTION_NAME
import kotlinx.coroutines.tasks.await

class FirestoreDataImpl(private val firestore: FirebaseFirestore) : FirestoreData {

    override suspend fun getAlbums(): NetworkStatus<List<AlbumMetadata>> {
        return try {
            val data = firestore.collection(ALBUM_COLLECTION_NAME).get().await()
                .toObjects(AlbumMetadata::class.java)
            if (data.isEmpty()) return NetworkStatus.Error(null, "Empty list")
            NetworkStatus.Success(data)
        } catch (e: Exception) {
            NetworkStatus.Error(e, e.message.toString())
        }
    }

    override suspend fun getAllSongs(): NetworkStatus<List<SongMetadata>> {
        return try {
            val data = firestore.collection(SONG_COLLECTION_NAME).get().await()
                .toObjects(SongMetadata::class.java)
            if (data.isEmpty()) return NetworkStatus.Error(null, "Empty list")
            data.sortBy { it.trackNumber }
            NetworkStatus.Success(data)
        } catch (e: Exception) {
            NetworkStatus.Error(e, e.message.toString())
        }
    }

    override suspend fun getSongsByAlbumName(albumName: String): NetworkStatus<List<SongMetadata>> {
        return try {
            val data = firestore.collection(SONG_COLLECTION_NAME).whereEqualTo("albumName", albumName).get().await()
                .toObjects(SongMetadata::class.java)
            if (data.isEmpty()) return NetworkStatus.Error(null, "Empty list")
            data.sortBy { it.trackNumber }
            NetworkStatus.Success(data)
        } catch (e: Exception) {
            NetworkStatus.Error(e, e.message.toString())
        }
    }
}