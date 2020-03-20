package com.malikane.mussic.Database.Dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.malikane.mussic.Database.Music2Playlist

@Dao
interface Music2PlaylistDao{
    /*@Query("INSERT INTO music2playlist(musicId,musicName,playlistId,playlistName) VALUES(:musicId,:musicName,:playlistId,:playlistName)")
    fun addSong(musicId:Int,musicName:String,playlistId:Int,playlistName:String)*/

    @Insert
    fun addedSong(music2Playlist: Music2Playlist)
    @Update
    fun updateTable(music2Playlist: Music2Playlist)
    @Delete
    fun deleted_A_Song(music2Playlist: Music2Playlist)

    @Query("SELECT * FROM music2playlist")
    fun getSongs_that_havePlaylist():LiveData<List<Music2Playlist>>

    @Query("SELECT * FROM music2playlist WHERE playlistId=:playlistId")
    fun getSpecificPlaylistSong(playlistId:Int):LiveData<List<Music2Playlist>>
}