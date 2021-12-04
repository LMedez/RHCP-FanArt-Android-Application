package com.luc.common.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep /* Proguard rule */
@Parcelize
data class AlbumMetadata(
    val title: String = "",
    val released: String = "",
    val recorded: String = "",
    val imageUrl: String = "",
    val producer: String = "",
    val artist: String = "",
    val trackCount: String = ""
) : Parcelable
