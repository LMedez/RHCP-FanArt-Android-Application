package com.luc.musicservice

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.luc.common.capitalizeFirstChar
import com.luc.musicservice.MusicPlayerConstants.Companion.NOTIFICATION_CHANNEL_ID
import com.luc.musicservice.MusicPlayerConstants.Companion.NOTIFICATION_ID
import kotlinx.coroutines.*

class MusicNotificationManager(
    private val context: Context,
    sessionToken: MediaSessionCompat.Token,
    notificationListener: PlayerNotificationManager.NotificationListener,
) {
    private val notificationManager: PlayerNotificationManager
    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    init {
        val mediaController = MediaControllerCompat(context, sessionToken)
        notificationManager =
            PlayerNotificationManager.Builder(context, NOTIFICATION_ID, NOTIFICATION_CHANNEL_ID)
                .setChannelNameResourceId(R.string.notification_channel_name)
                .setChannelDescriptionResourceId(R.string.notification_channel_description)
                .setMediaDescriptionAdapter(DescriptionAdapter(mediaController))
                .setNotificationListener(notificationListener)
                .setSmallIconResourceId(R.drawable.ic_music_note).build()
                .apply {
                    setMediaSessionToken(sessionToken)
                    // Don't display the rewind or fast-forward buttons.
                    setUseRewindAction(false)
                    setUseFastForwardAction(false)
                }
    }

    fun showNotification(player: Player) {
        notificationManager.setPlayer(player)
    }

    fun hideNotification() {
        notificationManager.setPlayer(null)
    }

    private inner class DescriptionAdapter(private val mediaControllerCompat: MediaControllerCompat) :
        PlayerNotificationManager.MediaDescriptionAdapter {

        var currentIconUri: Uri? = null
        var currentBitmap: Bitmap? = null

        override fun getCurrentContentTitle(player: Player): CharSequence {
            return mediaControllerCompat.metadata.description.title.toString()
                .capitalizeFirstChar()
        }

        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            return mediaControllerCompat.sessionActivity
        }

        override fun getCurrentContentText(player: Player): CharSequence? {
            return mediaControllerCompat.metadata.description.subtitle.toString()
                .capitalizeFirstChar()
        }

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback,
        ): Bitmap? {
            val iconUri = mediaControllerCompat.metadata.description.iconUri
            return if (currentIconUri != iconUri || currentBitmap == null) {

                // Cache the bitmap for the current song so that successive calls to
                // `getCurrentLargeIcon` don't cause the bitmap to be recreated.
                currentIconUri = iconUri
                serviceScope.launch {
                    currentBitmap = iconUri?.let {
                        resolveUriAsBitmap(it)
                    }
                    currentBitmap?.let { callback.onBitmap(it) }
                }
                null
            } else {
                currentBitmap
            }
        }

        private suspend fun resolveUriAsBitmap(uri: Uri): Bitmap? {
            return withContext(Dispatchers.IO) {
                // Block on downloading artwork.
                Glide.with(context).applyDefaultRequestOptions(glideOptions)
                    .asBitmap()
                    .load(uri)
                    .submit(NOTIFICATION_LARGE_ICON_SIZE, NOTIFICATION_LARGE_ICON_SIZE)
                    .get()
            }
        }
    }
}

const val NOTIFICATION_LARGE_ICON_SIZE = 144 // px

private val glideOptions = RequestOptions()
    .fallback(R.drawable.notification_error)
    .error(R.drawable.notification_error)
    .diskCacheStrategy(DiskCacheStrategy.DATA)