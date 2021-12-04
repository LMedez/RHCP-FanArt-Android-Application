package com.luc.common.entities.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.luc.common.entities.PlaylistEntity
import com.luc.common.entities.SongMetadataEntity

data class PlaylistWithSongs(
    @Embedded val playlistEntity: PlaylistEntity,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "id")
    val songs: List<SongMetadataEntity>,
)
