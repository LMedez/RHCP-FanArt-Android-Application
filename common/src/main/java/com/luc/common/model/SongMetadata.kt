package com.luc.common.model

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentId

@Keep
data class SongMetadata(
    @DocumentId val id: String = "",
    val title: String = "",
    val albumName: String = "",
    val imageUrl: String = "",
    val source: String = "",
    val artist: String = "",
    val trackNumber: Long = 0,
    val songDuration: String = ""
)
