package com.malikane.mussic.ViewModel.Repository

import android.app.Application
import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.malikane.mussic.Database.Artist
import com.malikane.mussic.Database.Dao.Music2PlaylistDao
import com.malikane.mussic.Database.Dao.MusicDao
import com.malikane.mussic.Database.Music
import com.malikane.mussic.Database.Music2Playlist
import com.malikane.mussic.Database.MussicDatabase


open class MusicRepository (application: Application){
    private var musicDao:MusicDao?
    private var allMusic:LiveData<List<Music>>?
    var db:MussicDatabase?=MussicDatabase.statics.getAppDataBase(application)
    init {
        musicDao= db?.musicDao()
        allMusic= musicDao!!.getAllMusic()
    }
    fun addSong(music:Music){
        AddSongAsyncTask(musicDao).execute(music)
    }
    fun updateSong(music: Music){
        UpdateSongAsyncTask(musicDao).execute(music)
    }
    fun deleteSong(music: Music){
        DeleteSongAsyncTask(musicDao).execute(music)
    }
    fun getArtist(music: Music):Artist{
        return GetArtistAsyncTask(musicDao).execute(music).get()
    }
    fun getAllSong():LiveData<List<Music>>?{
        return allMusic
    }
    //statics members
    companion object statics{
        private class AddSongAsyncTask(private var musicDao: MusicDao?): AsyncTask<Music, Void, Void>() {
            override fun doInBackground(vararg params: Music): Void? {
                musicDao!!.addMusic(params[0])
                return null
            }
        }
        private class UpdateSongAsyncTask(private var musicDao: MusicDao?): AsyncTask<Music, Void, Void>() {
            override fun doInBackground(vararg params: Music): Void? {
                musicDao!!.updateMusic(params[0])
                return null
            }
        }
        private class DeleteSongAsyncTask(private var musicDao: MusicDao?): AsyncTask<Music, Void, Void>() {
            override fun doInBackground(vararg params: Music): Void? {
                musicDao!!.deleteMusic(params[0])
                return null
            }
        }
        private class GetArtistAsyncTask(private var musicDao: MusicDao?): AsyncTask<Music, Void, Artist>() {
            override fun doInBackground(vararg params: Music): Artist? {
                return musicDao!!.getArtist(params[0].artistId,params[0].artistName)

            }
        }
    }
}