package com.luc.musicservice

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.luc.common.resource.Event
import com.luc.common.resource.NetworkStatus
import com.luc.musicservice.MusicPlayerConstants.Companion.NETWORK_ERROR
import com.luc.musicservice.extensions.EMPTY_PLAYBACK_STATE
import com.luc.musicservice.extensions.NOTHING_PLAYING
import com.luc.musicservice.extensions.id
import com.luc.musicservice.listeners.MediaChanges


class MusicServiceConnection(
    context: Context,
) {

    private val _isConnected = MutableLiveData<Event<NetworkStatus<Boolean>>>()
    val isConnected: LiveData<Event<NetworkStatus<Boolean>>> = _isConnected

    private val _networkError = MutableLiveData<Event<NetworkStatus<Boolean>>>()
    val networkError: LiveData<Event<NetworkStatus<Boolean>>> = _networkError

    private val _playbackState = MutableLiveData<PlaybackStateCompat>()
    val playbackState: LiveData<PlaybackStateCompat> = _playbackState

    private val _curPlayingSong = MutableLiveData<MediaMetadataCompat>()
    val curPlayingSong: LiveData<MediaMetadataCompat> = _curPlayingSong

    private val _mediaItems = MutableLiveData<MutableList<MediaBrowserCompat.MediaItem>>()
    val mediaItems: LiveData<MutableList<MediaBrowserCompat.MediaItem>> = _mediaItems

    lateinit var mediaController: MediaControllerCompat

    private val mediaBrowserConnectionCallback = MediaBrowserConnectionCallback(context)

    private val mediaBrowser = MediaBrowserCompat(
        context,
        ComponentName(
            context,
            MusicService::class.java
        ),
        mediaBrowserConnectionCallback,
        null
    ).apply { connect() }

    val transportControls: MediaControllerCompat.TransportControls
        get() = mediaController.transportControls

    fun subscribe(
        parentId: String,
        playNow: Boolean = false,
    ) {
        mediaBrowser.subscribe(parentId, subscriptionCallback(playNow))
    }

    fun unsubscribe(parentId: String) {
        mediaBrowser.unsubscribe(parentId, subscriptionCallback())
    }

    private fun subscriptionCallback(playNow: Boolean = false) =
        object : MediaBrowserCompat.SubscriptionCallback() {
            private fun setMediaItems(mutableList: MutableList<MediaBrowserCompat.MediaItem>) {
                if (mutableList.isNotEmpty()) {
                    _mediaItems.value = mutableList
                    if (playNow) transportControls.playFromMediaId(_mediaItems.value?.get(0)?.mediaId,
                        null)
                } else {
                    _mediaItems.postValue(mutableListOf())
                }
            }

            override fun onChildrenLoaded(
                parentId: String,
                children: MutableList<MediaBrowserCompat.MediaItem>,
            ) {
                super.onChildrenLoaded(parentId, children)
                setMediaItems(children)
            }
        }

    private inner class MediaBrowserConnectionCallback(
        private val context: Context,
    ) : MediaBrowserCompat.ConnectionCallback() {

        override fun onConnected() {
            mediaController = MediaControllerCompat(context, mediaBrowser.sessionToken).apply {
                registerCallback(MediaControllerCallback())
            }

            _isConnected.postValue(Event(NetworkStatus.Success(true)))
        }

        override fun onConnectionSuspended() {
            _isConnected.postValue(
                Event(
                    NetworkStatus.Error(
                        null, "The connection was suspended"
                    )
                )
            )
        }

        override fun onConnectionFailed() {
            _isConnected.postValue(
                Event(
                    NetworkStatus.Error(
                        null, "Couldn't connect to media browser"
                    )
                )
            )
        }
    }

    private inner class MediaControllerCallback : MediaControllerCompat.Callback() {

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            _playbackState.postValue(state ?: EMPTY_PLAYBACK_STATE)
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            if (_curPlayingSong.value?.id != null && _curPlayingSong.value?.id == metadata?.id) {
                return
            }
            _curPlayingSong.postValue(
                if (metadata?.id == null) {
                    NOTHING_PLAYING
                } else {
                    metadata
                }
            )
        }

        override fun onSessionEvent(event: String?, extras: Bundle?) {
            super.onSessionEvent(event, extras)
            when (event) {
                NETWORK_ERROR -> _networkError.postValue(
                    Event(
                        NetworkStatus.Error(
                            null,
                            "Couldn't connect to the server. Please check your internet connection."
                        )
                    )
                )
            }
        }

        override fun onSessionDestroyed() {
            mediaBrowserConnectionCallback.onConnectionSuspended()
        }
    }

    companion object {
        private val _mediaDuration = MutableLiveData<Long>()
        val mediaDuration: LiveData<Long> = _mediaDuration
        private val _shuffleMode = MutableLiveData<Boolean>(false)
        val shuffleMode: LiveData<Boolean> = _shuffleMode

        val onMediaChanges = object : MediaChanges {
            override fun onMediaDurationChange(duration: Long) {
                _mediaDuration.postValue(duration)
            }

            override fun onMediaShuffleModeChange(boolean: Boolean) {
                _shuffleMode.postValue(boolean)
            }
        }


    }
}

