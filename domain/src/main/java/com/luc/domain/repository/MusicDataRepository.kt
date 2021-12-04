package com.luc.domain.repository

import com.luc.common.entities.*
import com.luc.common.entities.relations.PlaylistWithSongs
import com.luc.common.model.AlbumMetadata
import com.luc.common.model.SongMetadata
import com.luc.common.resource.NetworkStatus
import kotlinx.coroutines.flow.Flow

interface MusicDataRepository {
    fun getLastSongsPlayed() : Flow<List<LastSongPlayedEntity>>
    fun getPlayList(): Flow<List<PlaylistWithSongs>>
    fun getSongsByQuery(query:String): Flow<List<SongMetadataEntity>>
    suspend fun deleteFavSong(songMetadata: SongMetadata)
    suspend fun deleteFavAlbum(albumMetadata: AlbumMetadata)
    suspend fun insertLastSongPlayed(songMetadata: SongMetadata)
    suspend fun addPlayList(playlistEntity: PlaylistEntity, playlistSongEntity: List<SongMetadataEntity>)
    suspend fun addFavSong(songMetadata: SongMetadata)
    suspend fun getFavSong(): Flow<List<FavSongsEntity>>
    suspend fun getFavAlbums(): Flow<List<FavAlbumEntity>>
    suspend fun addFavAlbum(albumMetadata: AlbumMetadata)
    suspend fun getAlbums(): NetworkStatus<List<AlbumMetadata>>
    suspend fun getAllSongs(): NetworkStatus<List<SongMetadata>>
    suspend fun getSongs(limit: Int = 15):NetworkStatus<List<SongMetadata>>
    suspend fun getRandomSongs(): NetworkStatus<List<SongMetadata>>
    suspend fun getSongsByAlbumName(albumName: String): NetworkStatus<List<SongMetadata>>
}