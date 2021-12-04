package com.luc.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.luc.common.entities.*
import com.luc.common.entities.relations.PlaylistSongCrossRef
import com.luc.data.utils.Converters

@Database(entities = [
    SongMetadataEntity::class,
    AlbumMetadataEntity::class,
    FavSongsEntity::class,
    PlaylistEntity::class,
    FavAlbumEntity::class,
    LastSongPlayedEntity::class,
    PlaylistSongCrossRef::class],
    version = 1)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun musicDao(): LocalMusicDAO
}