package com.luc.presentation.viewmodel

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.luc.common.model.SongMetadata

class PlaylistViewModel : ViewModel() {

    val currentPlaylistSongs = mutableListOf<SongMetadata>()

    private val _showAlbumImage = MutableLiveData<String?>()
    val showAlbumImage: LiveData<String?> = _showAlbumImage

    private val _clearPlaylist = MutableLiveData<Boolean>()
    val clearPlaylist: LiveData<Boolean> = _clearPlaylist

    private val _playlistSongAdded = MutableLiveData<SongMetadata>()
    val playlistSongAdded: LiveData<Int> = Transformations.map(_playlistSongAdded) {
        if (currentPlaylistSongs.isNotEmpty()) {
            if (currentPlaylistSongs.last().albumName != it.albumName)
                _showAlbumImage.postValue(it.imageUrl)
            else _showAlbumImage.postValue(null)
        } else _showAlbumImage.postValue(it.imageUrl)
        currentPlaylistSongs.add(it)
        countTimer.start()
        currentPlaylistSongs.size
    }

    fun addSongToPlaylist(songMetadata: SongMetadata) {
        _playlistSongAdded.postValue(songMetadata)
    }

    private val countTimer = object : CountDownTimer(1500, 1000) {
        override fun onTick(millisUntilFinished: Long) {}
        override fun onFinish() {
            _showAlbumImage.postValue(null)
        }
    }

    fun cancelTimerAnimation() {
        countTimer.cancel()
    }

    fun clearPlaylist() {
        _clearPlaylist.postValue(true)
        onCleared()
    }

    override fun onCleared() {
        super.onCleared()
        countTimer.cancel()
        currentPlaylistSongs.clear()
    }
}