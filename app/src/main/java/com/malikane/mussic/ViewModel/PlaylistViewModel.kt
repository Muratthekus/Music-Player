package com.malikane.mussic.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.malikane.mussic.Database.Music
import com.malikane.mussic.Database.PlaylistBase
import com.malikane.mussic.ViewModel.Repository.PlaylistRepository

class PlaylistViewModel(application: Application):AndroidViewModel(application){
    private var repository:PlaylistRepository= PlaylistRepository(application)
    private var allPlaylist:LiveData<List<PlaylistBase>>?=repository.getAllPlaylist()

    fun createPlaylist(playlistBase: PlaylistBase)=repository.createPlaylist(playlistBase)

    fun updatePlaylist(playlistBase: PlaylistBase)=repository.updatePlaylist(playlistBase)

    fun deletePlaylist(playlistBase: PlaylistBase)=repository.deletePlaylist(playlistBase)

    fun getAllPlaylist():LiveData<List<PlaylistBase>>?{
        return repository.getAllPlaylist()
    }
}