package com.luc.presentation.viewmodel

import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.*
import com.luc.common.Constants.EXPLORE_MUSIC
import com.luc.common.model.SongMetadata
import com.luc.musicservice.MusicServiceConnection
import com.luc.musicservice.extensions.*
import kotlin.math.floor

class MusicPlayerViewModel constructor(
    musicServiceConnection: MusicServiceConnection,
) : ViewModel() {

    private var mediaId: String? = null

    private val handler = Handler(Looper.getMainLooper())
    private var updatePosition = true

    private val _playbackStates = MutableLiveData<PlayBackStates>()
    val playbackStates: LiveData<PlayBackStates> = _playbackStates

    private var playback: PlaybackStateCompat = EMPTY_PLAYBACK_STATE

    val mediaDuration: LiveData<Long> =
        Transformations.map(MusicServiceConnection.mediaDuration) { duration ->
            if (duration > 0) {
                checkPlaybackPosition()
                duration
            } else -1L
        }

    val shuffleModeEnabled = MusicServiceConnection.shuffleMode

    private val _mediaPosition = MutableLiveData<Long>().apply {
        postValue(0L)
    }
    val mediaPosition: LiveData<Long> = Transformations.map(_mediaPosition) {
        it
    }

    private val playbackStateObserver = Observer<PlaybackStateCompat> {
        playback = it ?: EMPTY_PLAYBACK_STATE
        updatePlaybackState(playback)
    }

    private val musicServiceConnection = musicServiceConnection.also {
        it.playbackState.observeForever(playbackStateObserver)
    }

    private fun updatePlaybackState(playbackState: PlaybackStateCompat) {
        when (playbackState.state) {
            PlaybackStateCompat.STATE_BUFFERING -> _playbackStates.postValue(PlayBackStates.STATE_BUFFERING)
            PlaybackStateCompat.STATE_PLAYING -> {
                checkPlaybackPosition()
                _playbackStates.postValue(PlayBackStates.STATE_PLAYING)
            }
            PlaybackStateCompat.STATE_PAUSED -> _playbackStates.postValue(PlayBackStates.STATE_PAUSED)
            else -> _playbackStates.postValue(PlayBackStates.STATE_EMPTY)
        }
    }

    private var _showMediaPlayer = MutableLiveData<Boolean>()
    val showMediaPlayer: LiveData<Boolean> = _showMediaPlayer

    val songsMedia = Transformations.map(musicServiceConnection.mediaItems) { mutableList ->
        mutableList.map { it.asSongMetadataMedia() }
    }

    val mediaSongPlaying = Transformations.map(musicServiceConnection.curPlayingSong) {
        setShowMediaPlayer(true)
        it.asSongMetadataMedia()
    }

    fun skipToNextSong() {
        musicServiceConnection.transportControls.skipToNext()
    }

    fun skipToPreviousSong() {
        musicServiceConnection.transportControls.skipToPrevious()
    }

    fun seekTo(pos: Long) {
        setShowMediaPlayer(true)
        musicServiceConnection.transportControls.seekTo(pos)
    }

    fun enableShuffleMode() {
        musicServiceConnection.transportControls.setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_ALL)
    }

    fun disableShuffleMode() {
        musicServiceConnection.transportControls.setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_NONE)
    }

    fun playOrToggleSong(songMetadata: SongMetadata, toggle: Boolean = false) {
        val isPrepared = playback.isPrepared
        if (isPrepared && songMetadata.id == musicServiceConnection.curPlayingSong.value?.id) {
            playback.let { playbackState ->
                when {
                    playbackState.isPlaying -> if (toggle) musicServiceConnection.transportControls.pause()
                    playbackState.isPlayEnabled -> musicServiceConnection.transportControls.play()
                    else -> Unit
                }
            }
        } else {
            musicServiceConnection.transportControls.playFromMediaId(songMetadata.id, null)
        }
    }


    fun playAlbum(albumName: String) {
        subscribeToMediaItems(albumName, true)
    }


    fun playPauseMediaPlayer() {
        if (playback.isPrepared) {
            playback.let { playbackState ->
                when {
                    playbackState.isPlaying -> musicServiceConnection.transportControls.pause()
                    playbackState.isPlayEnabled -> musicServiceConnection.transportControls.play()
                    else -> Unit
                }
            }
        }
    }

    private fun setShowMediaPlayer(value: Boolean) {
        countTimer.cancel()
        countTimer.start()
        _showMediaPlayer.postValue(value)
    }

    fun switchMediaPlayerVisibility(visible: Boolean) {
        setShowMediaPlayer(visible)
    }

    fun unSubscribeToMediaItems() {
        musicServiceConnection.unsubscribe(mediaId ?: EXPLORE_MUSIC)
    }

    fun subscribeToMediaItems(
        parentId: String,
        playNow: Boolean = false,
    ) {
        mediaId = parentId
        if (playNow) musicServiceConnection.subscribe(parentId, playNow)
        else musicServiceConnection.subscribe(parentId)
    }

    fun timestampToMSS(position: Long): String {
        val totalSeconds = floor(position / 1E3).toInt()
        val minutes = totalSeconds / 60
        val remainingSeconds = totalSeconds - (minutes * 60)
        return if (position <= 0) "0:00"
        else "%d:%02d".format(minutes, remainingSeconds)
    }

    private fun checkPlaybackPosition(): Boolean = handler.postDelayed({
        val currPosition = playback.currentPlayBackPosition
        if (_mediaPosition.value != currPosition && currPosition <= mediaDuration.value ?: return@postDelayed)
            _mediaPosition.postValue(currPosition)
        if (updatePosition)
            checkPlaybackPosition()
    }, POSITION_UPDATE_INTERVAL_MILLIS)


    private val countTimer = object : CountDownTimer(4000, 1000) {
        override fun onTick(millisUntilFinished: Long) {}
        override fun onFinish() {
            _showMediaPlayer.postValue(false)
        }
    }

    enum class PlayBackStates {
        STATE_BUFFERING,
        STATE_PLAYING,
        STATE_EMPTY,
        STATE_PAUSED
    }

    override fun onCleared() {
        super.onCleared()
        countTimer.cancel()
        musicServiceConnection.unsubscribe(mediaId ?: EXPLORE_MUSIC)
        musicServiceConnection.playbackState.removeObserver(playbackStateObserver)
        updatePosition = false
    }

    private fun <T> LiveData<T>.observeOnce(observer: Observer<T>) {
        observeForever(object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }
}


private const val POSITION_UPDATE_INTERVAL_MILLIS = 100L