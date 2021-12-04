package com.luc.common.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.luc.common.model.AlbumMetadata

@Entity(tableName = "albums")
data class AlbumMetadataEntity(
    @PrimaryKey(autoGenerate = false) val title: String = "",
    val released: String = "",
    val recorded: String = "",
    val imageUrl: String = "",
    val producer: String = "",
    val artist: String = "",
    val trackCount: String = "",
)

// AlbumMetadataEntity -> AlbumMetadata
fun AlbumMetadataEntity.asAlbumMetadata(): AlbumMetadata {
    return AlbumMetadata(
        this.title,
        this.released,
        this.recorded,
        this.imageUrl,
        this.producer,
        this.artist,
        this.trackCount)
}

fun List<AlbumMetadataEntity>.asAlbumMetadata(): List<AlbumMetadata> {
    return this.map {
        it.asAlbumMetadata()
    }
}

// AlbumMetadata -> AlbumMetadataEntity
fun AlbumMetadata.asAlbumMetadataEntity(): AlbumMetadataEntity {
    return AlbumMetadataEntity(
        this.title,
        this.released,
        this.recorded,
        this.imageUrl,
        this.producer,
        this.artist,
        this.trackCount)
}

fun List<AlbumMetadata>.asAlbumMetadataEntity(): List<AlbumMetadataEntity> {
    return this.map {
        it.asAlbumMetadataEntity()
    }
}
