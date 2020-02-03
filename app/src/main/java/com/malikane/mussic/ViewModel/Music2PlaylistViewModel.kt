package com.malikane.mussic.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.malikane.mussic.Database.Music2Playlist
import com.malikane.mussic.ViewModel.Repository.Music2PlaylistRepository

class Music2PlaylistViewModel(application: Application):AndroidViewModel(application){
    private var repository:Music2PlaylistRepository= Music2PlaylistRepository(application)
    private var allPlaylistedSong:LiveData<List<Music2Playlist>>?=repository.allplaylistedSong

    fun addedASong(music2Playlist: Music2Playlist)=repository.addedASong(music2Playlist)
    fun updateTable(music2Playlist: Music2Playlist)=repository.updateTable(music2Playlist)
    fun deletedASong(music2Playlist: Music2Playlist)=repository.deletedASong(music2Playlist)
    fun getAllPlaylistedSong():LiveData<List<Music2Playlist>>{
       return repository.getSongsThatHavePlaylist()
    }
    fun getSpecificPlaylistSong(playlistId:Int):LiveData<List<Music2Playlist>>{
        return repository.getSpecificPlaylistSong(playlistId)
    }
}