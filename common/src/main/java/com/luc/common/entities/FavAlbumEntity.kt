package com.luc.common.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.luc.common.model.AlbumMetadata

@Entity
data class FavAlbumEntity(
    @PrimaryKey(autoGenerate = false) val title: String = "",
    val released: String = "",
    val recorded: String = "",
    val imageUrl: String = "",
    val producer: String = "",
    val artist: String = "",
    val trackCount: String = "",
)

// AlbumMetadataEntity -> AlbumMetadata
fun FavAlbumEntity.asAlbumMetadata(): AlbumMetadata {
    return AlbumMetadata(
        this.title,
        this.released,
        this.recorded,
        this.imageUrl,
        this.producer,
        this.artist,
        this.trackCount)
}

fun List<FavAlbumEntity>.asAlbumMetadata(): List<AlbumMetadata> {
    return this.map {
        it.asAlbumMetadata()
    }
}

// AlbumMetadata -> AlbumMetadataEntity
fun AlbumMetadata.asFavAlbumEntity(): FavAlbumEntity {
    return FavAlbumEntity(
        this.title,
        this.released,
        this.recorded,
        this.imageUrl,
        this.producer,
        this.artist,
        this.trackCount)
}

fun List<AlbumMetadata>.asFavAlbumEntity(): List<FavAlbumEntity> {
    return this.map {
        it.asFavAlbumEntity()
    }
}
