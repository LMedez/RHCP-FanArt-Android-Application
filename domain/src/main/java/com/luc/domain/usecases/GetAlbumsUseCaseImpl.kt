package com.luc.domain.usecases

import com.luc.common.entities.FavAlbumEntity
import com.luc.common.model.AlbumMetadata
import com.luc.common.resource.NetworkStatus
import com.luc.domain.repository.MusicDataRepository
import kotlinx.coroutines.flow.Flow

interface GetAlbumsUseCase {
    suspend fun getAlbums() : NetworkStatus<List<AlbumMetadata>>
    suspend fun getFavAlbums() : Flow<List<FavAlbumEntity>>

}

class GetAlbumsUseCaseImpl(private val musicDataRepository: MusicDataRepository) :
    GetAlbumsUseCase {
    override suspend fun getAlbums() = musicDataRepository.getAlbums()
    override suspend fun getFavAlbums() = musicDataRepository.getFavAlbums()
}