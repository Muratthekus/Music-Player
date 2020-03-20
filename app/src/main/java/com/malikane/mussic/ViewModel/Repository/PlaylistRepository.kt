package com.malikane.mussic.ViewModel.Repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.malikane.mussic.Database.Dao.Music2PlaylistDao
import com.malikane.mussic.Database.Dao.PlaylistDao
import com.malikane.mussic.Database.Music
import com.malikane.mussic.Database.Music2Playlist
import com.malikane.mussic.Database.MussicDatabase
import com.malikane.mussic.Database.PlaylistBase

class PlaylistRepository(application: Application) {
    private var playlistDao:PlaylistDao?
    private var allPlaylist:LiveData<List<PlaylistBase>>?
    var db:MussicDatabase?= MussicDatabase.getAppDataBase(application)
    init {
        playlistDao=db?.playlistDao()
        allPlaylist=playlistDao!!.getAllPlaylist()
    }
    fun createPlaylist(playlistBase: PlaylistBase){
        createPlaylistAsyncTask(playlistDao).execute(playlistBase)
    }
    fun updatePlaylist(playlistBase: PlaylistBase){
        updatePlaylistAsyncTask(playlistDao).execute(playlistBase)
    }
    fun deletePlaylist(playlistBase: PlaylistBase){
        deletePlaylistAsyncTask(playlistDao).execute(playlistBase)
    }

    fun getAllPlaylist():LiveData<List<PlaylistBase>>?{
        return allPlaylist
    }
    companion object statics{
        private class createPlaylistAsyncTask(private var playlistDao: PlaylistDao?):AsyncTask<PlaylistBase,Void,Void>(){
            override fun doInBackground(vararg params: PlaylistBase): Void? {
                playlistDao!!.createPlaylist(params[0])
                return null
            }
        }
        private class updatePlaylistAsyncTask(private var playlistDao: PlaylistDao?):AsyncTask<PlaylistBase,Void,Void>(){
            override fun doInBackground(vararg params: PlaylistBase): Void? {
                playlistDao!!.updatePlaylist(params[0])
                return null
            }
        }
        private class deletePlaylistAsyncTask(private var playlistDao: PlaylistDao?):AsyncTask<PlaylistBase,Void,Void>(){
            override fun doInBackground(vararg params: PlaylistBase): Void? {
                playlistDao!!.deletePlaylist(params[0])
                return null
            }
        }

    }
}