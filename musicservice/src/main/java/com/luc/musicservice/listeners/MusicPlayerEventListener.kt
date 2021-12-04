package com.luc.musicservice.listeners

import android.util.Log
import android.widget.Toast
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.metadata.Metadata
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.luc.musicservice.MusicNotificationManager
import com.luc.musicservice.MusicService

class MusicPlayerEventListener(
    private val musicService: MusicService,
    private val notificationManager: MusicNotificationManager,
    private val player: Player,
    private val playbackIsReady: () -> Unit,
    private val shuffleModeChanges: (Boolean) -> Unit
) : Player.Listener {

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)

        Toast.makeText(
            musicService,
            "Error to check the song source, please check your connection and try again",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        super.onPlayWhenReadyChanged(playWhenReady, playbackState)
        if (playbackState == Player.STATE_READY) {
            playbackIsReady()
        }
        when (playbackState) {
            Player.STATE_BUFFERING,
            Player.STATE_READY -> {
                notificationManager.showNotification(player)
                if (!playWhenReady)
                    musicService.stopForeground(false)
            }
            else -> notificationManager.hideNotification()
        }
    }

    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
        super.onShuffleModeEnabledChanged(shuffleModeEnabled)
        shuffleModeChanges(shuffleModeEnabled)
    }
}