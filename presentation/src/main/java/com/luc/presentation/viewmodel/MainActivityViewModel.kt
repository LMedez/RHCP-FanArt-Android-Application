package com.luc.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.luc.common.resource.Event
import com.luc.common.resource.NetworkStatus
import com.luc.musicservice.MusicServiceConnection

class MainActivityViewModel(musicServiceConnection: MusicServiceConnection) :
    ViewModel() {

    val onNetworkError: LiveData<Event<NetworkStatus<Boolean>>> =
        Transformations.map(musicServiceConnection.networkError) {
            it
        }

    val onConnectionFailed: LiveData<Event<NetworkStatus<Boolean>>> =
        Transformations.map(musicServiceConnection.isConnected) {
            it
        }
}