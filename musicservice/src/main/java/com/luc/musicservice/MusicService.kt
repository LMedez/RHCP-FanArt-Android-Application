package com.luc.musicservice

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.ControlDispatcher
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.REPEAT_MODE_ALL
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.luc.musicservice.MusicPlayerConstants.Companion.EMPTY_ROOT
import com.luc.musicservice.MusicPlayerConstants.Companion.NETWORK_ERROR
import com.luc.musicservice.MusicPlayerConstants.Companion.SERVICE_TAG
import com.luc.musicservice.extensions.*
import com.luc.musicservice.listeners.MediaChanges
import com.luc.musicservice.listeners.MusicPlaybackPrepared
import com.luc.musicservice.listeners.MusicPlayerEventListener
import com.luc.musicservice.listeners.MusicPlayerNotificationListener
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject

class MusicService : MediaBrowserServiceCompat() {

    private val dataSourceFactory by inject<DefaultDataSourceFactory>()

    private val exoPlayer by inject<SimpleExoPlayer>()

    private val musicSource by inject<DataSource>()

    private val mediaChanges: MediaChanges = MusicServiceConnection.onMediaChanges

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private var currentPlaylistItems: List<MediaMetadataCompat> = emptyList()

    private lateinit var musicNotificationManager: MusicNotificationManager

    var isForegroundService = false

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector

    private var currPlayingSong: MediaMetadataCompat? = null

    private lateinit var musicPlayerEventListener: MusicPlayerEventListener

    override fun onCreate() {
        super.onCreate()

        // Intent to reenter into activity on notification's click
        val activityIntent = packageManager?.getLaunchIntentForPackage(packageName)?.let {
            PendingIntent.getActivity(this, 0, it, 0)
        }

        // creates a mediasession and pass the activity intent
        mediaSession = MediaSessionCompat(this, SERVICE_TAG).apply {
            setSessionActivity(activityIntent)
            isActive = true
        }


        // set the mediaSession of this service
        sessionToken = mediaSession.sessionToken

        musicNotificationManager = MusicNotificationManager(
            this,
            mediaSession.sessionToken,
            MusicPlayerNotificationListener(this)
        )

        val musicPlaybackPrepared = MusicPlaybackPrepared(musicSource) { mediaMetadata ->
            currPlayingSong = mediaMetadata
            preparePlayer(musicSource.sortedBy { it.trackNumber }, mediaMetadata, true)
        }

        // the mediasession's connector connect the mediasession with the player
        mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlaybackPreparer(musicPlaybackPrepared)
        mediaSessionConnector.setQueueNavigator(MusicQueueNavigator())
        mediaSessionConnector.setPlayer(exoPlayer)

        musicPlayerEventListener =
            MusicPlayerEventListener(this, musicNotificationManager, exoPlayer, playbackIsReady = {
                serviceScope.launch {
                    musicSource.saveRecentSongPlayed(currentPlaylistItems[exoPlayer.currentWindowIndex])
                }
                mediaChanges.onMediaDurationChange(exoPlayer.duration)
            }) { inShuffleMode ->
                mediaChanges.onMediaShuffleModeChange(inShuffleMode)
            }


        exoPlayer.addListener(musicPlayerEventListener)

        exoPlayer.repeatMode = REPEAT_MODE_ALL

        musicNotificationManager.showNotification(exoPlayer)
    }

    private fun preparePlayer(
        songs: List<MediaMetadataCompat>,
        itemToPlay: MediaMetadataCompat?,
        playNow: Boolean,
    ) {
        currentPlaylistItems = songs
        val curSongIndex =
            if (currPlayingSong == null) 0 else songs.indexOfFirst { it.title == itemToPlay?.title }
        exoPlayer.setMediaSource(songs.toMediaSource(dataSourceFactory))
        exoPlayer.prepare()
        exoPlayer.seekTo(curSongIndex, 0L)
        exoPlayer.playWhenReady = playNow
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?,
    ): BrowserRoot {
        return BrowserRoot(EMPTY_ROOT, null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>,
    ) {

        serviceScope.launch {
            musicSource.getSongs(parentId)
        }

        loadChildrenImpl(result)
    }

    private fun loadChildrenImpl(
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>,
    ) {
        val resultSent = musicSource.whenReady { isInitialized ->
            if (isInitialized) {
                val children = musicSource.map { item ->
                    MediaBrowserCompat.MediaItem(
                        MediaDescriptionCompat.Builder().from(item),
                        item.flag
                    )
                }.toMutableList()
                result.sendResult(children)
            } else {
                mediaSession.sendSessionEvent(NETWORK_ERROR, null)
                result.sendResult(null)
            }
        }

        if (!resultSent) {
            result.detach()
        }

    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        exoPlayer.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        exoPlayer.removeListener(musicPlayerEventListener)
        exoPlayer.release()
    }

    private inner class MusicQueueNavigator : TimelineQueueNavigator(mediaSession) {
        override fun getMediaDescription(
            player: Player,
            windowIndex: Int,
        ): MediaDescriptionCompat {
            return currentPlaylistItems[windowIndex].description
        }
    }
}
