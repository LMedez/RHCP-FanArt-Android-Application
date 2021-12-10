package com.luc.data.local

import com.luc.common.entities.*
import com.luc.common.entities.relations.PlaylistSongCrossRef
import com.luc.common.model.AlbumMetadata
import com.luc.common.model.SongMetadata

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

    suspend fun insertPlayList(
        playlist: PlaylistEntity,
        songMetadataList: List<SongMetadataEntity>,
    ) {
        val playlistId = localMusicDAO.insertPlaylist(playlist)
        songMetadataList.forEach {
            localMusicDAO.insertPlaylistSongCrossRef(PlaylistSongCrossRef(playlistId = playlistId, it.id))
        }

    }

    fun getPlayList() =
        localMusicDAO.getPlayList()

}