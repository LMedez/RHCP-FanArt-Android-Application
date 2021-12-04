package com.luc.domain.usecases

import com.luc.common.model.AlbumMetadata
import com.luc.common.model.SongMetadata
import com.luc.domain.repository.MusicDataRepository

interface DeleteSongsUseCase {
    suspend fun deleteFavSong(songMetadata: SongMetadata)
    suspend fun deleteFavAlbum(albumMetadata: AlbumMetadata)
}

class DeleteSongsUseCaseImpl(private val musicDataRepository: MusicDataRepository) :
    DeleteSongsUseCase {


    override suspend fun deleteFavSong(songMetadata: SongMetadata) {
        musicDataRepository.deleteFavSong(songMetadata)
    }

    override suspend fun deleteFavAlbum(albumMetadata: AlbumMetadata) =
        musicDataRepository.deleteFavAlbum(albumMetadata)
}