package com.luc.musicservice.listeners

interface MediaChanges {
    fun onMediaDurationChange(duration: Long)
    fun onMediaShuffleModeChange(boolean: Boolean)
}