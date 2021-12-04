package com.luc.common.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.luc.common.getCurrentDateTime
import com.luc.common.toFormat
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Long = 0,
    val playlistName: String = "",
    val playlistDescription: String = "",
    val dateCreated: String = getCurrentDateTime().toFormat("yyyy/MM/dd HH:mm:ss"),
)

