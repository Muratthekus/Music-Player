package com.malikane.mussic.ViewModel.Repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.malikane.mussic.Database.Artist
import com.malikane.mussic.Database.Dao.ArtistDao
import com.malikane.mussic.Database.Music
import com.malikane.mussic.Database.MussicDatabase

class ArtistRepository(application: Application){
    private var artistDao : ArtistDao?
    private var allArtist:LiveData<List<Artist>>?
    var db:MussicDatabase?= MussicDatabase.getAppDataBase(application)
    init{
        artistDao=db?.artistDao()
        allArtist=artistDao!!.getAllArtist()
    }
    fun addArtist(artist: Artist){
        AddArtistAsyncTask(artistDao).execute(artist)
    }
    fun updateArtist(artist: Artist){
        UpdateArtistAsyncTask(artistDao).execute(artist)
    }
    fun deleteArtist(artist: Artist){
        DeleteArtistAsyncTask(artistDao).execute(artist)
    }
    fun getArtistSong(artist:Artist):LiveData<List<Music>>{
        return GetArtistSongAsyncTask(artistDao).execute(artist).get()
    }
    fun getAllArtist():LiveData<List<Artist>>?{
        return  allArtist
    }
    companion object statics{
        private class AddArtistAsyncTask(private var artistDao: ArtistDao?):AsyncTask<Artist, Void, Void>() {
            override fun doInBackground(vararg params: Artist): Void? {
                artistDao!!.addArtist(params[0])
                return null
            }

        }
        private class UpdateArtistAsyncTask(private var artistDao: ArtistDao?):AsyncTask<Artist, Void, Void>() {
            override fun doInBackground(vararg params: Artist): Void? {
                artistDao!!.updateArtist(params[0])
                return null
            }

        }
        private class DeleteArtistAsyncTask(private var artistDao: ArtistDao?):AsyncTask<Artist, Void, Void>() {
            override fun doInBackground(vararg params: Artist): Void? {
                artistDao!!.deleteArtist(params[0])
                return null
            }

        }
        private class GetArtistSongAsyncTask(private var artistDao: ArtistDao?):AsyncTask<Artist, Void, LiveData<List<Music>>>() {
            override fun doInBackground(vararg params: Artist): LiveData<List<Music>>? {
                return artistDao!!.getArtistSong(params[0].Id)

            }

        }
    }

}