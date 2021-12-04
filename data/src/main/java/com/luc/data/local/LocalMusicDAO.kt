package com.luc.data.local

import androidx.room.*
import com.luc.common.entities.*
import com.luc.common.entities.AlbumMetadataEntity
import com.luc.common.entities.relations.PlaylistSongCrossRef
import com.luc.common.entities.relations.PlaylistWithSongs
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalMusicDAO {
    @Query("SELECT * FROM SongMetadataEntity")
    suspend fun getAllSongs(): List<SongMetadataEntity>

    @Query("SELECT * FROM SongMetadataEntity WHERE title LIKE :query OR albumName LIKE :query")
    fun getSongsByQuery(query: String) : Flow<List<SongMetadataEntity>>

    @Query("SELECT * FROM SongMetadataEntity WHERE albumName = :albumName")
    suspend fun getSongsByAlbumName(albumName: String): List<SongMetadataEntity>

    @Query("SELECT * FROM favSongs")
    fun getFavSongs() : Flow<List<FavSongsEntity>>

    @Query("SELECT * FROM FavAlbumEntity")
    fun getFavAlbums() : Flow<List<FavAlbumEntity>>

    @Query("SELECT * FROM albums")
    suspend fun getAllAlbums(): List<AlbumMetadataEntity>

    @Query("SELECT * FROM LastSongPlayedEntity ORDER BY joined DESC LIMIT :limit")
    fun getLastSongsPlayed(limit: Int = 15): Flow<List<LastSongPlayedEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = SongMetadataEntity::class)
    suspend fun insertSongs(songsEntity: List<SongMetadataEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = SongMetadataEntity::class)
    suspend fun insertSong(songsEntity: SongMetadataEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = FavSongsEntity::class)
    suspend fun insertFavSong(favSongsEntity: FavSongsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = FavAlbumEntity::class)
    suspend fun insertFavAlbum(favAlbumEntity: FavAlbumEntity)

    @Delete(entity = FavSongsEntity::class)
    suspend fun deleteFavSong(favSongsEntity: FavSongsEntity)

    @Delete(entity = FavAlbumEntity::class)
    suspend fun deleteFavAlbum(favAlbumEntity: FavAlbumEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = AlbumMetadataEntity::class)
    suspend fun insertAlbum(albumMetadataEntity: List<AlbumMetadataEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLastSongPlayed(lastSongPlayedEntity: LastSongPlayedEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistSongCrossRef(crossRef: PlaylistSongCrossRef)

    @Transaction
    @Query("SELECT * FROM playlistentity")
    fun getPlayList() : Flow<List<PlaylistWithSongs>>


}