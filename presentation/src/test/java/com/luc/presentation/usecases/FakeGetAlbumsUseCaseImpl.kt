package com.luc.presentation.usecases

import com.luc.common.model.AlbumMetadata
import com.luc.common.resource.NetworkStatus
import com.luc.domain.usecases.GetAlbumsUseCase

class FakeGetAlbumsUseCaseImpl : GetAlbumsUseCase {

    private val albumList = mutableListOf(
        AlbumMetadata(),
        AlbumMetadata(),
        AlbumMetadata(),
        AlbumMetadata(),
        AlbumMetadata()
    )

    override suspend fun getAlbums(): NetworkStatus<List<AlbumMetadata>> {
        return if (albumList.isNotEmpty()) NetworkStatus.Success(albumList)
        else NetworkStatus.Error(null, "The album list is empty")
    }
}