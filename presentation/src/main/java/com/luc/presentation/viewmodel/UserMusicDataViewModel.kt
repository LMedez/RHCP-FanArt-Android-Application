package com.luc.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.luc.common.entities.PlaylistEntity
import com.luc.common.entities.asAlbumMetadata
import com.luc.common.entities.asSongMetadata
import com.luc.common.entities.asSongMetadataEntity
import com.luc.common.model.AlbumMetadata
import com.luc.common.model.SongMetadata
import com.luc.domain.usecases.AddSongsUseCase
import com.luc.domain.usecases.DeleteSongsUseCase
import com.luc.domain.usecases.GetAlbumsUseCase
import com.luc.domain.usecases.GetSongsUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class UserMusicDataViewModel(
    private val addSongsUseCase: AddSongsUseCase,
    private val deleteSongsUseCase: DeleteSongsUseCase,
    private val getSongsUseCase: GetSongsUseCase,
    private val getAlbumsUseCase: GetAlbumsUseCase
) : ViewModel() {

    private var favSongList = mutableListOf<SongMetadata>()
    private var favAlbumList = mutableListOf<AlbumMetadata>()

    private val _playlistTitle = MutableLiveData<Boolean>()
    val playlistTitle: LiveData<Boolean> = _playlistTitle

    val favSongs = liveData {
        getSongsUseCase.getFavSongs().collect {
            favSongList = it.asSongMetadata().toMutableList()
            emit(favSongList.toList())
        }
    }

    val favAlbums = liveData {
        getAlbumsUseCase.getFavAlbums().collect {
            favAlbumList = it.asAlbumMetadata().toMutableList()
            emit(favAlbumList.toList())
        }
    }

    val getLastSongsPlayedEntity = liveData {
        getSongsUseCase.getLastSongsPlayed().collect {
            emit(it)
        }
    }

    fun addOrRemoveFavSong(songMetadata: SongMetadata) {
        favSongList.filter { it.albumName == songMetadata.albumName }
        viewModelScope.launch {
            if (favSongList.contains(songMetadata)) {
                deleteSongsUseCase.deleteFavSong(songMetadata)
            } else addSongsUseCase.addFavSong(songMetadata)
        }
    }

    fun addOrRemoveFavAlbum(albumMetadata: AlbumMetadata) {
        favAlbumList.filter { it.title == albumMetadata.title }
        viewModelScope.launch {
            if (favAlbumList.contains(albumMetadata)) {
                deleteSongsUseCase.deleteFavAlbum(albumMetadata)
            } else addSongsUseCase.addFavAlbum(albumMetadata)
        }
    }

    /**
     * This will be improved in next release
     */

    private var preparePlaylist = PreparePlaylist()

    fun getPlayList() = liveData {
        getSongsUseCase.getPlayList().collect {
            emit(it)
        }
    }

    fun preparePlayList(preparePlaylistFrom: PreparePlaylist? = null): PreparePlaylist {
        preparePlaylistFrom?.let {
            return it
        } ?: return preparePlaylist
    }

    fun clearPlaylist() {
        preparePlaylist = PreparePlaylist()
    }

    fun createPlayList() {
        if (preparePlaylist.playlistName.isEmpty()) {
            _playlistTitle.postValue(false)
            return
        }

        if (preparePlaylist.songs.isEmpty()) return

        viewModelScope.launch {
            addSongsUseCase.addPlaylist(
                PlaylistEntity(
                    playlistName = preparePlaylist.playlistName,
                    playlistDescription = preparePlaylist.playlistDescription),
                playlistSongEntity = preparePlaylist.songs.map {
                    it.asSongMetadataEntity()
                })
        }


    }

    data class PreparePlaylist(
        var playlistName: String = "",
        var playlistDescription: String = "",
        val songs: MutableList<SongMetadata> = mutableListOf(),
    )
}
