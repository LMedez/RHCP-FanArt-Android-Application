package com.luc.common.entities.relations

import androidx.room.Entity

@Entity(primaryKeys = ["playlistId", "id"])
    data class PlaylistSongCrossRef(
        val playlistId: Long,
        val id: String
    )
