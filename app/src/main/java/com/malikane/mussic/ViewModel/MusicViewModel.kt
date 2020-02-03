package com.malikane.mussic.ViewModel

import android.app.Application
import android.content.Context
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.malikane.mussic.Database.Artist
import com.malikane.mussic.Database.Music
import com.malikane.mussic.ViewModel.Repository.MusicRepository

open class MusicViewModel(@NonNull application: Application) : AndroidViewModel(application){
    private var repository:MusicRepository = MusicRepository(application)
    private var allMusic:LiveData<List<Music>>?=repository.getAllSong()

    fun addSong(music: Music)=repository.addSong(music)

    fun updateSong(music: Music)=repository.updateSong(music)

    fun deleteSong(music: Music)=repository.deleteSong(music)

    fun getArtist(music: Music):Artist{
        return repository.getArtist(music)
    }
    fun getAllMusic():LiveData<List<Music>>?{
        return allMusic
    }

}