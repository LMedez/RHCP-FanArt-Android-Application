package com.luc.domain.usecases

import com.luc.common.entities.PlaylistEntity
import com.luc.common.entities.SongMetadataEntity
import com.luc.common.model.AlbumMetadata
import com.luc.common.model.SongMetadata
import com.luc.domain.repository.MusicDataRepository

interface AddSongsUseCase {
    suspend fun addFavAlbum(albumMetadata: AlbumMetadata)
    suspend fun addLastSongPlayed(songMetadata: SongMetadata)
    suspend fun addFavSong(songMetadata: SongMetadata)
    suspend fun addPlaylist(
        playlistEntity: PlaylistEntity,
        playlistSongEntity: List<SongMetadataEntity>,
    )
}

class AddSongsUseCaseImpl(private val musicDataRepository: MusicDataRepository) :
    AddSongsUseCase {
    override suspend fun addFavAlbum(albumMetadata: AlbumMetadata) =
        musicDataRepository.addFavAlbum(albumMetadata)

    override suspend fun addLastSongPlayed(songMetadata: SongMetadata) =
        musicDataRepository.insertLastSongPlayed(songMetadata)

    override suspend fun addFavSong(songMetadata: SongMetadata) {
        musicDataRepository.addFavSong(songMetadata)
    }

    override suspend fun addPlaylist(
        playlistEntity: PlaylistEntity,
        playlistSongEntity: List<SongMetadataEntity>,
    ) {
        musicDataRepository.addPlayList(playlistEntity, playlistSongEntity)
    }

}