package com.luc.common.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.luc.common.model.SongMetadata

@Entity(tableName = "favSongs")
data class FavSongsEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String = "",
    val title: String = "",
    val albumName: String = "",
    val imageUrl: String = "",
    val source: String = "",
    val artist: String = "",
    val trackNumber: Long = 0,
    val songDuration: String = "",
)

// SongMetadataEntity -> SongMetadata
fun FavSongsEntity.asSongMetadata(): SongMetadata {
    return SongMetadata(
        this.id,
        this.title,
        this.albumName,
        this.imageUrl,
        this.source,
        this.artist,
        this.trackNumber)
}

fun List<FavSongsEntity>.asSongMetadata(): List<SongMetadata> {
    return this.map {
        it.asSongMetadata()
    }
}

// SongMetadata -> FavSongsEntity
fun SongMetadata.asFavSongsEntity(): FavSongsEntity {
    return FavSongsEntity(this.id,
        this.title,
        this.albumName,
        this.imageUrl,
        this.source,
        this.artist,
        this.trackNumber)
}

fun List<SongMetadata>.asFavSongsEntity(): List<FavSongsEntity> {
    return this.map {
        it.asFavSongsEntity()
    }
}