package com.luc.musicservice.extensions

import android.graphics.Bitmap
import android.net.Uri
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.luc.common.model.SongMetadata

/**
 * Useful extensions for [MediaMetadataCompat].
 */
inline val MediaMetadataCompat.id: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)

inline val MediaMetadataCompat.title: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_TITLE)

inline val MediaMetadataCompat.artist: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_ARTIST)

inline val MediaMetadataCompat.duration
    get() = getLong(MediaMetadataCompat.METADATA_KEY_DURATION)

inline val MediaMetadataCompat.album: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_ALBUM)

inline val MediaMetadataCompat.author: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_AUTHOR)

inline val MediaMetadataCompat.writer: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_WRITER)

inline val MediaMetadataCompat.composer: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_COMPOSER)

inline val MediaMetadataCompat.compilation: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_COMPILATION)

inline val MediaMetadataCompat.date: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_DATE)

inline val MediaMetadataCompat.year: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_YEAR)

inline val MediaMetadataCompat.genre: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_GENRE)

inline val MediaMetadataCompat.trackNumber
    get() = getLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER)

inline val MediaMetadataCompat.trackCount
    get() = getLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS)

inline val MediaMetadataCompat.discNumber
    get() = getLong(MediaMetadataCompat.METADATA_KEY_DISC_NUMBER)

inline val MediaMetadataCompat.albumArtist: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST)

inline val MediaMetadataCompat.art: Bitmap
    get() = getBitmap(MediaMetadataCompat.METADATA_KEY_ART)

inline val MediaMetadataCompat.artUri: Uri
    get() = this.getString(MediaMetadataCompat.METADATA_KEY_ART_URI).toUri()

inline val MediaMetadataCompat.albumArt: Bitmap?
    get() = getBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART)

inline val MediaMetadataCompat.albumArtUri: Uri
    get() = this.getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI).toUri()

inline val MediaMetadataCompat.userRating
    get() = getLong(MediaMetadataCompat.METADATA_KEY_USER_RATING)

inline val MediaMetadataCompat.rating
    get() = getLong(MediaMetadataCompat.METADATA_KEY_RATING)

inline val MediaMetadataCompat.displayTitle: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE)

inline val MediaMetadataCompat.displaySubtitle: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE)

inline val MediaMetadataCompat.displayDescription: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION)

inline val MediaMetadataCompat.displayIcon: Bitmap
    get() = getBitmap(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON)

inline val MediaMetadataCompat.displayIconUri: Uri
    get() = this.getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI).toUri()

inline val MediaMetadataCompat.mediaUri: Uri
    get() = this.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI).toUri()

inline val MediaMetadataCompat.downloadStatus
    get() = getLong(MediaMetadataCompat.METADATA_KEY_DOWNLOAD_STATUS)

inline val MediaMetadataCompat.country: String?
    get() = this.getString(METADATA_KEY_COUNTRY)

inline val MediaMetadataCompat.campus: String?
    get() = this.getString(METADATA_KEY_CAMPUS)

inline val MediaMetadataCompat.released: String?
    get() = this.getString(METADATA_KEY_RELEASED)

inline val MediaMetadataCompat.producer: String?
    get() = this.getString(METADATA_KEY_PRODUCER)


/*
 * Custom property for storing whether a [MediaMetadataCompat] item represents an
 * item that is [MediaItem.FLAG_BROWSABLE] or [MediaItem.FLAG_PLAYABLE].
 */
inline val MediaMetadataCompat.flag
    get() = this.getLong(METADATA_KEY_FLAGS).toInt()

/*
 * Useful extensions for [MediaMetadataCompat.Builder].
 */

fun MediaMetadataCompat.Builder.from(songMetadata: SongMetadata): MediaMetadataCompat.Builder {
    id = songMetadata.id
    mediaUri = songMetadata.source
    title = songMetadata.title
    album = songMetadata.albumName
    artist = songMetadata.artist
    trackNumber = songMetadata.trackNumber
    albumArtUri = songMetadata.imageUrl
    flag = MediaItem.FLAG_PLAYABLE

    // To make things easier for *displaying* these, set the display properties as well.
    displaySubtitle = songMetadata.albumName

    displayDescription = songMetadata.artist
    displayIconUri = songMetadata.imageUrl // Used by ExoPlayer and Notification
    albumArtUri = songMetadata.imageUrl

    // Add downloadStatus to force the creation of an "extras" bundle in the resulting
    // MediaMetadataCompat object. This is needed to send accurate metadata to the
    // media session during updates.
    downloadStatus = MediaDescriptionCompat.STATUS_NOT_DOWNLOADED

    // Allow it to be used in the typical builder style.
    return this
}

fun MediaItem.asSongMetadataMedia(): SongMetadata {
    return SongMetadata(
        this.mediaId!!,
        this.description.title.toString(),
        this.description.subtitle.toString(),
        this.description.iconUri.toString(),
        this.description.mediaUri.toString(),
        this.description.extras?.getString(MediaMetadataCompat.METADATA_KEY_ARTIST)
            ?: "",
        this.description.extras?.getLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER)!!
    )
}

fun MediaMetadataCompat.asSongMetadataMedia(): SongMetadata {
    return SongMetadata(
        this.description.mediaId!!,
        this.title ?: "",
        this.album ?: "",
        this.albumArtUri.toString(),
        this.mediaUri.toString(),
        this.artist ?: "",
        this.trackNumber
    )
}

fun MediaDescriptionCompat.Builder.from(mediaMetadataCompat: MediaMetadataCompat): MediaDescriptionCompat {

    setTitle(mediaMetadataCompat.description.title)
    setSubtitle(mediaMetadataCompat.album)
    setMediaId(mediaMetadataCompat.description.mediaId)
    setIconUri(mediaMetadataCompat.description.iconUri)
    setMediaUri(mediaMetadataCompat.mediaUri)

    val bundle = bundleOf(
        MediaMetadataCompat.METADATA_KEY_ARTIST to mediaMetadataCompat.artist,
        MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER to mediaMetadataCompat.trackNumber
    )

    setExtras(bundle)
    return this.build()
}


// These do not have getters, so create a message for the error.
const val NO_GET = "Property does not have a 'get'"

inline var MediaMetadataCompat.Builder.id: String
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, value)
    }

inline var MediaMetadataCompat.Builder.title: String?
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_TITLE, value)
    }

inline var MediaMetadataCompat.Builder.artist: String?
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_ARTIST, value)
    }

inline var MediaMetadataCompat.Builder.album: String?
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_ALBUM, value)
    }

inline var MediaMetadataCompat.Builder.duration: Long
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putLong(MediaMetadataCompat.METADATA_KEY_DURATION, value)
    }

inline var MediaMetadataCompat.Builder.genre: String?
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_GENRE, value)
    }

inline var MediaMetadataCompat.Builder.mediaUri: String?
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, value)
    }

inline var MediaMetadataCompat.Builder.albumArtUri: String?
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, value)
    }

inline var MediaMetadataCompat.Builder.albumArt: Bitmap?
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, value)
    }

inline var MediaMetadataCompat.Builder.trackNumber: Long
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, value)
    }

inline var MediaMetadataCompat.Builder.trackCount: Long
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, value)
    }

inline var MediaMetadataCompat.Builder.displayTitle: String?
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, value)
    }

inline var MediaMetadataCompat.Builder.displaySubtitle: String?
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, value)
    }

inline var MediaMetadataCompat.Builder.displayDescription: String?
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION, value)
    }

inline var MediaMetadataCompat.Builder.displayIconUri: String?
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, value)
    }

inline var MediaMetadataCompat.Builder.downloadStatus: Long
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putLong(MediaMetadataCompat.METADATA_KEY_DOWNLOAD_STATUS, value)
    }

/**
 * Custom property for storing whether a [MediaMetadataCompat] item represents an
 * item that is [MediaItem.FLAG_BROWSABLE] or [MediaItem.FLAG_PLAYABLE].
 */
inline var MediaMetadataCompat.Builder.flag: Int
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putLong(METADATA_KEY_FLAGS, value.toLong())
    }

inline var MediaMetadataCompat.Builder.country: String?
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putString(METADATA_KEY_COUNTRY, value)
    }

inline var MediaMetadataCompat.Builder.campus: String?
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putString(METADATA_KEY_CAMPUS, value)
    }

inline var MediaMetadataCompat.Builder.released: String?
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putString(METADATA_KEY_RELEASED, value)
    }

inline var MediaMetadataCompat.Builder.producer: String?
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putString(METADATA_KEY_PRODUCER, value)
    }

inline var MediaMetadataCompat.Builder.date: String?
    @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
    get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
    set(value) {
        putString(MediaMetadataCompat.METADATA_KEY_DATE, value)
    }

/**
 * Extension method for building an [ExtractorMediaSource] from a [MediaMetadataCompat] object.
 *
 * For convenience, place the [MediaDescriptionCompat] into the tag so it can be retrieved later.
 */
fun MediaMetadataCompat.toMediaSource(dataSourceFactory: DataSource.Factory) =
    ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri)

/**
 * Extension method for building a [ConcatenatingMediaSource] given a [List]
 * of [MediaMetadataCompat] objects.
 */
fun List<MediaMetadataCompat>.toMediaSource(
    dataSourceFactory: DataSource.Factory
): ConcatenatingMediaSource {

    val concatenatingMediaSource = ConcatenatingMediaSource()
    forEach {
        concatenatingMediaSource.addMediaSource(it.toMediaSource(dataSourceFactory))
    }
    return concatenatingMediaSource
}

val NOTHING_PLAYING: MediaMetadataCompat = MediaMetadataCompat.Builder()
    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, "")
    .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, "")
    .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, 0)
    .build()

val EMPTY_PLAYBACK_STATE: PlaybackStateCompat = PlaybackStateCompat.Builder()
    .setState(PlaybackStateCompat.STATE_NONE, 0, 0f)
    .build()


/**
 * Custom property that holds whether an item is [MediaItem.FLAG_BROWSABLE] or
 * [MediaItem.FLAG_PLAYABLE].
 */
const val METADATA_KEY_FLAGS =
    "com.example.android.redhotapplication.musicplayer.METADATA_KEY_FLAGS"

const val METADATA_KEY_COUNTRY =
    "com.example.android.redhotapplication.musicplayer.METADATA_KEY_COUNTRY"

const val METADATA_KEY_CAMPUS =
    "com.example.android.redhotapplication.musicplayer.METADATA_KEY_CAMPUS"

const val METADATA_KEY_RELEASED =
    "com.example.android.redhotapplication.musicplayer.METADATA_KEY_RELEASED"

const val METADATA_KEY_PRODUCER =
    "com.example.android.redhotapplication.musicplayer.METADATA_KEY_PRODUCER"
