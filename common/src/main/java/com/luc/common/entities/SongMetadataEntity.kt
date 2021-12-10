package com.luc.common.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.luc.common.model.SongMetadata

@Entity
data class SongMetadataEntity (
    @PrimaryKey(autoGenerate = false)
    val id: String = "",
    val title: String = "",
    val albumName: String = "",
    val imageUrl: String = "",
    val source: String = "",
    val artist: String = "",
    val trackNumber: Long = 0,
)

// SongMetadataEntity -> SongMetadata
fun SongMetadataEntity.asSongMetadata(): SongMetadata {
    return SongMetadata(
        this.id,
        this.title,
        this.albumName,
        this.imageUrl,
        this.source,
        this.artist,
        this.trackNumber)
}

fun List<SongMetadataEntity>.asSongMetadata(): List<SongMetadata> {
    return this.map {
        it.asSongMetadata()
    }
}

// SongMetadata -> SongMetadataEntity
fun SongMetadata.asSongMetadataEntity(): SongMetadataEntity {
    return SongMetadataEntity(
        this.id,
        this.title,
        this.albumName,
        this.imageUrl,
        this.source,
        this.artist,
        this.trackNumber)
}

fun List<SongMetadata>.asSongMetadataEntity(): List<SongMetadataEntity> {
    return this.map {
        it.asSongMetadataEntity()
    }
}