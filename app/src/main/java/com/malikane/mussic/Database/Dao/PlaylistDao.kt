package com.malikane.mussic.Database.Dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.malikane.mussic.Database.Music
import com.malikane.mussic.Database.Music2Playlist
import com.malikane.mussic.Database.PlaylistBase

@Dao
interface PlaylistDao{
    @Insert
    fun createPlaylist(playlistBase: PlaylistBase)

    @Update
    fun updatePlaylist(playlistBase: PlaylistBase)

    @Delete
    fun deletePlaylist(playlistBase: PlaylistBase)

    @Query("SELECT * FROM music LEFT JOIN music2playlist ON music.Id=music2playlist.musicId INNER JOIN playlistBase ON music2playlist.playlistId=playlistBase.Id WHERE playlistBase.Id=:playlistId")
    fun getAllSong(playlistId: Int):LiveData<List<Music>>

    @Query("SELECT * FROM playlistBase")
    fun getAllPlaylist():LiveData<List<PlaylistBase>>
}