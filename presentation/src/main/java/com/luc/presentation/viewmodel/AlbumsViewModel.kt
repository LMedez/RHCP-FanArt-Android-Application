package com.luc.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.luc.common.resource.NetworkStatus
import com.luc.domain.usecases.GetAlbumsUseCase

class AlbumsViewModel(private val albumsUseCase: GetAlbumsUseCase) : ViewModel() {

    fun getAllAlbums() = liveData {
        emit(NetworkStatus.Loading)
        emit(albumsUseCase.getAlbums())
    }

}