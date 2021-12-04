package com.luc.data.local

import android.util.Log
import com.luc.common.entities.*
import com.luc.common.entities.relations.PlaylistSongCrossRef
import com.luc.common.model.AlbumMetadata
import com.luc.common.model.SongMetadata
import kotlinx.coroutines.flow.collect

class LocalMusicSource(private val localMusicDAO: LocalMusicDAO) {

    suspend fun getSongsByAlbumName(albumName: String): List<SongMetadata> {
        return localMusicDAO.getSongsByAlbumName(albumName).asSongMetadata()
    }

    suspend fun getAllSongs() = localMusicDAO.getAllSongs().asSongMetadata()

    fun getSongsByQuery(query: String) = localMusicDAO.getSongsByQuery(query)

    fun getFavSongs() = localMusicDAO.getFavSongs()

    fun getFavAlbums() = localMusicDAO.getFavAlbums()

    suspend fun deleteFavSong(songMetadata: SongMetadata) =
        localMusicDAO.deleteFavSong(songMetadata.asFavSongsEntity())

    suspend fun deleteFavAlbum(albumMetadata: AlbumMetadata) =
        localMusicDAO.deleteFavAlbum(albumMetadata.asFavAlbumEntity())

    suspend fun insertSongs(songMetadata: List<SongMetadata>) =
        localMusicDAO.insertSongs(songMetadata.asSongMetadataEntity())

    suspend fun insertFavSong(songMetadata: SongMetadata) =
        localMusicDAO.insertFavSong(songMetadata.asFavSongsEntity())

    suspend fun insertFavAlbum(albumMetadata: AlbumMetadata) =
        localMusicDAO.insertFavAlbum(albumMetadata.asFavAlbumEntity())

    suspend fun insertAlbum(albumMetadata: List<AlbumMetadata>) =
        localMusicDAO.insertAlbum(albumMetadata.asAlbumMetadataEntity())

    suspend fun insertLastSongPlayed(lastSongPlayedEntity: LastSongPlayedEntity) {
        localMusicDAO.insertLastSongPlayed(lastSongPlayedEntity)
    }

    suspend fun getAllAlbums() = localMusicDAO.getAllAlbums().asAlbumMetadata()

    fun getLastSongsPlayed(limit: Int = 15) = localMusicDAO.getLastSongsPlayed(limit)

    // Work in future releases
    suspend fun addPlayList(
        playlistEntity: PlaylistEntity,
        songMetadataList: List<SongMetadataEntity>,
    ) {
        songMetadataList.forEach {
            localMusicDAO.insertSong(it)
        }
        localMusicDAO.insertPlaylist(playlistEntity)
        songMetadataList.forEach {
            localMusicDAO.insertPlaylistSongCrossRef(
                PlaylistSongCrossRef(
                    playlistId = playlistEntity.playlistId,
                    id = it.id)
            )
        }


        localMusicDAO.getPlayList().collect { list ->
            list.forEach {
                Log.d("tests", it.songs.size.toString())
            }
        }
    }

    fun getPlayList() =
        localMusicDAO.getPlayList()

}