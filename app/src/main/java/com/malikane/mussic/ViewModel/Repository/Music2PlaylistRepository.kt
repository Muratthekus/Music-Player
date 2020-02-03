package com.malikane.mussic.ViewModel.Repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.malikane.mussic.Database.Dao.Music2PlaylistDao
import com.malikane.mussic.Database.Music2Playlist

import com.malikane.mussic.Database.MussicDatabase

open class Music2PlaylistRepository (application: Application){
    private var music2PlaylistDao: Music2PlaylistDao?
     var allplaylistedSong: LiveData<List<Music2Playlist>>
    var db: MussicDatabase?= MussicDatabase.statics.getAppDataBase(application)
    init {
        music2PlaylistDao= db?.mussic2playlistDao()
        allplaylistedSong=music2PlaylistDao!!.getSongs_that_havePlaylist()
    }
    fun addedASong(music2Playlist: Music2Playlist){
        AddedASongAsyncTask(music2PlaylistDao).execute(music2Playlist)
    }
    fun updateTable(music2Playlist: Music2Playlist){
        UpdateTableAsyncTask(music2PlaylistDao).execute(music2Playlist)
    }
    fun deletedASong(music2Playlist: Music2Playlist){
        DeletedASongAsyncTask(music2PlaylistDao)
    }
    fun getSongsThatHavePlaylist():LiveData<List<Music2Playlist>>{
        return GetSongThatHavePlaylist(music2PlaylistDao).execute().get()
    }
    fun getSpecificPlaylistSong(playlistId: Int):LiveData<List<Music2Playlist>>{
        return GetSpecificPlaylistSong(music2PlaylistDao,playlistId).execute().get()
    }

    //statics members
    companion object statics{
       private class AddedASongAsyncTask(private var music2PlaylistDao: Music2PlaylistDao?):AsyncTask<Music2Playlist,Void,Void>(){
           override fun doInBackground(vararg params: Music2Playlist): Void? {
               music2PlaylistDao!!.addedSong(params[0])
               return null
           }

       }
        private class UpdateTableAsyncTask(private var music2PlaylistDao: Music2PlaylistDao?):AsyncTask<Music2Playlist,Void,Void>(){
            override fun doInBackground(vararg params: Music2Playlist): Void? {
                music2PlaylistDao!!.updateTable(params[0])
                return null
            }

        }
        private class DeletedASongAsyncTask(private var music2PlaylistDao: Music2PlaylistDao?):AsyncTask<Music2Playlist,Void,Void>(){
            override fun doInBackground(vararg params: Music2Playlist): Void? {
                music2PlaylistDao!!.deleted_A_Song(params[0])
                return null
            }

        }
        private class GetSongThatHavePlaylist(private var music2PlaylistDao: Music2PlaylistDao?):AsyncTask<Void,Void,LiveData<List<Music2Playlist>>>(){
            override fun doInBackground(vararg params: Void?): LiveData<List<Music2Playlist>> {
                return music2PlaylistDao!!.getSongs_that_havePlaylist()
            }
        }
        private class GetSpecificPlaylistSong(private var music2PlaylistDao: Music2PlaylistDao?,private var playlistId:Int):AsyncTask<Void,Void,LiveData<List<Music2Playlist>>>(){
            override fun doInBackground(vararg params: Void?): LiveData<List<Music2Playlist>> {
                return music2PlaylistDao!!.getSpecificPlaylistSong(playlistId)
            }
        }
    }
}