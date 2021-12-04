package com.luc.presentation.viewmodel

import androidx.lifecycle.*
import com.luc.common.entities.asSongMetadata
import com.luc.common.model.SongMetadata
import com.luc.common.resource.NetworkStatus
import com.luc.domain.usecases.GetAlbumsUseCase
import com.luc.domain.usecases.GetSongsUseCase
import kotlinx.coroutines.flow.collect

class MusicDataViewModel(
    private val albumsUseCase: GetAlbumsUseCase,
    private val songsUseCase: GetSongsUseCase,
) : ViewModel() {

    private val _networkError = MutableLiveData<NetworkStatus.Error<*>>()
    val networkError : LiveData<NetworkStatus.Error<*>> = _networkError

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState : LiveData<Boolean> = _loadingState

    fun getAllSongs() = liveData {
        _loadingState.postValue(true)
        when(val result = songsUseCase.getAllSongs()) {
            is NetworkStatus.Success -> {
                _loadingState.postValue(false)
                emit(result.data)
            }
            is NetworkStatus.Error -> {
                _networkError.postValue(result)
            }
        }
    }

    fun getSongs(limit: Int = 15) = liveData {
        _loadingState.postValue(true)
        when(val result = songsUseCase.getSongs(limit)) {
            is NetworkStatus.Success -> {
                _loadingState.postValue(false)
                emit(result.data)
            }
            is NetworkStatus.Error -> {
                _networkError.postValue(result)
            }
        }
    }

    fun getAlbums() = liveData {
        _loadingState.postValue(true)
        when(val result = albumsUseCase.getAlbums()) {
            is NetworkStatus.Success -> {
                _loadingState.postValue(false)
                emit(result.data)
            }
            is NetworkStatus.Error -> {
                _networkError.postValue(result)
            }
        }
    }

    fun getSongsByQuery(query: String) = liveData {
        songsUseCase.getSongsByQuery(query).collect {
            emit(it.asSongMetadata())
        }
    }
}