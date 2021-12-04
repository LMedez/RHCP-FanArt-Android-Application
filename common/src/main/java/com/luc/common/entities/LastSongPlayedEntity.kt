package com.luc.common.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.luc.common.getCurrentDateTime
import com.luc.common.toFormat
import java.util.*

@Entity
data class LastSongPlayedEntity(
    @PrimaryKey(autoGenerate = false)
    val songId: String,
    @Embedded val songMetadataEntity: SongMetadataEntity,
    val joined: String = getCurrentDateTime().toFormat("yyyy/MM/dd HH:mm:ss")
)
