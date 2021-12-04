package com.luc.domain.usecases

import com.luc.common.entities.FavSongsEntity
import com.luc.common.entities.LastSongPlayedEntity
import com.luc.common.entities.SongMetadataEntity
import com.luc.common.entities.relations.PlaylistWithSongs
import com.luc.common.model.SongMetadata
import com.luc.common.resource.NetworkStatus
import com.luc.domain.repository.MusicDataRepository
import kotlinx.coroutines.flow.Flow

interface GetSongsUseCase {
    fun getLastSongsPlayed(limit: Int = 15) : Flow<List<LastSongPlayedEntity>>
    fun getPlayList() : Flow<List<PlaylistWithSongs>>
    suspend fun getFavSongs() : Flow<List<FavSongsEntity>>
    suspend fun getAllSongs(): NetworkStatus<List<SongMetadata>>
    suspend fun getSongs(limit: Int = 15): NetworkStatus<List<SongMetadata>>
    suspend fun getRandomSongs(): NetworkStatus<List<SongMetadata>>
    suspend fun getSongsByQuery(query:String): Flow<List<SongMetadataEntity>>
    suspend fun getSongsByAlbumName(albumName: String) : NetworkStatus<List<SongMetadata>>

}

class GetSongsUseCaseImpl constructor(private val musicDataRepository: MusicDataRepository) :
    GetSongsUseCase {
    override fun getLastSongsPlayed(limit: Int) = musicDataRepository.getLastSongsPlayed()
    override fun getPlayList() = musicDataRepository.getPlayList()
    override suspend fun getFavSongs() = musicDataRepository.getFavSong()
    override suspend fun getAllSongs() = musicDataRepository.getAllSongs()
    override suspend fun getSongs(limit: Int) = musicDataRepository.getSongs(limit)
    override suspend fun getRandomSongs() = musicDataRepository.getRandomSongs()
    override suspend fun getSongsByQuery(query: String) = musicDataRepository.getSongsByQuery(query)
    override suspend fun getSongsByAlbumName(albumName: String) = musicDataRepository.getSongsByAlbumName(albumName)


}