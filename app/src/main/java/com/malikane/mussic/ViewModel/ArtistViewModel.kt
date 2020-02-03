package com.malikane.mussic.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.malikane.mussic.Database.Artist
import com.malikane.mussic.Database.Music
import com.malikane.mussic.ViewModel.Repository.ArtistRepository

class ArtistViewModel(application: Application):AndroidViewModel(application){
    private var repository:ArtistRepository= ArtistRepository(application)
    private var allArtist:LiveData<List<Artist>>?=repository.getAllArtist()

    fun addArtist(artist:Artist)=repository.addArtist(artist)

    fun updateArtist(artist: Artist)=repository.updateArtist(artist)

    fun deleteArtist(artist: Artist)=repository.deleteArtist(artist)

    fun getAllArtist():LiveData<List<Artist>>?{
        return allArtist
    }
    fun getArtistSong(artist: Artist):LiveData<List<Music>>{
        return repository.getArtistSong(artist)
    }
}