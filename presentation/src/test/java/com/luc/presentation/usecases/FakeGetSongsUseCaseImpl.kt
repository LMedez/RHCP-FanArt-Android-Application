package com.luc.presentation.usecases

import com.luc.common.model.SongMetadata
import com.luc.common.resource.NetworkStatus
import com.luc.domain.usecases.GetSongsUseCase

class FakeGetSongsUseCaseImpl : GetSongsUseCase {

     val songList = mutableListOf(
        SongMetadata(),
        SongMetadata(),
        SongMetadata(),
        SongMetadata(),
        SongMetadata()
    )

    override suspend fun getAllSongs(): NetworkStatus<List<SongMetadata>> {
        return if(songList.isEmpty()) NetworkStatus.Error(null, "The song list is empty")
        else NetworkStatus.Success(songList)
    }
}