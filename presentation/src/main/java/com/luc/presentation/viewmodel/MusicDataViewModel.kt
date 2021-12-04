package com.luc.presentation.viewmodel

import androidx.lifecycle.*
import com.luc.common.entities.asSongMetadata
import com.luc.common.model.AlbumMetadata
import com.luc.common.model.SongMetadata
import com.luc.common.resource.NetworkStatus
import com.luc.domain.usecases.GetAlbumsUseCase
import com.luc.domain.usecases.GetSongsUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MusicDataViewModel(
    private val albumsUseCase: GetAlbumsUseCase,
    private val songsUseCase: GetSongsUseCase,
) : ViewModel() {

    private val _networkError = MutableLiveData<NetworkStatus.Error<*>?>()
    val networkError : LiveData<NetworkStatus.Error<*>?> = _networkError

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState : LiveData<Boolean> = _loadingState

    private val _limitSongs = MutableLiveData<NetworkStatus<List<SongMetadata>>>()
    val limitSongs: LiveData<List<SongMetadata>> = Transformations.map(_limitSongs) {
        when(val result = it) {
            is NetworkStatus.Success -> {
                _loadingState.postValue(false)
                result.data
            }
            is NetworkStatus.Error -> {
                _networkError.postValue(result)
                emptyList()
            }
            else -> emptyList()
        }
    }

    private val _albums = MutableLiveData<NetworkStatus<List<AlbumMetadata>>>()
    val albums: LiveData<List<AlbumMetadata>> = Transformations.map(_albums) {
        when(val result = it) {
            is NetworkStatus.Success -> {
                _loadingState.postValue(false)
                result.data
            }
            is NetworkStatus.Error -> {
                _networkError.postValue(result)
                emptyList()
            }
            else -> emptyList()
        }
    }

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

    fun getLimitSongs(limit: Int = 15) {
        _loadingState.postValue(true)
        _networkError.postValue(null)
        viewModelScope.launch {
            _limitSongs.postValue(songsUseCase.getSongs(limit))
        }
    }

    fun getAlbums() {
        _loadingState.postValue(true)
        _networkError.postValue(null)
        viewModelScope.launch {
            _albums.postValue(albumsUseCase.getAlbums())
        }
    }

    fun getSongsByQuery(query: String) = liveData {
        songsUseCase.getSongsByQuery(query).collect {
            emit(it.asSongMetadata())
        }
    }
}