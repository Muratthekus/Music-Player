package com.malikane.mussic.Database.Dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.malikane.mussic.Database.Artist
import com.malikane.mussic.Database.Music

@Dao
interface MusicDao {
    @Insert
    fun addMusic(music: Music)

    @Update
    fun updateMusic(music: Music)

    @Delete
    fun deleteMusic(music: Music)


    @Query("SELECT * FROM artist LEFT JOIN music ON artist.Id=music.artistId AND artist.name=music.artistName WHERE music.artistId=:artistId AND music.artistName=:artistName ")
    fun getArtist(artistId:Int,artistName:String):Artist

    @Query("SELECT * FROM music")
    fun getAllMusic(): LiveData<List<Music>>?

}