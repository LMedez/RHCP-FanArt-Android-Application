package com.luc.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.luc.common.resource.NetworkStatus
import com.luc.domain.usecases.GetSongsUseCase

class AlbumDetailViewModel(private val getSongsUseCase: GetSongsUseCase) : ViewModel() {

    fun getSongsByAlbum(albumName: String) = liveData {
        emit(NetworkStatus.Loading)
        emit(getSongsUseCase.getSongsByAlbumName(albumName))
    }
}