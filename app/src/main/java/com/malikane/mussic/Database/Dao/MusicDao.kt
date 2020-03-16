package com.malikane.mussic.Database.Dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.malikane.mussic.Database.Music

@Dao
interface MusicDao {
    @Insert
    fun addMusic(music: Music)

    @Update
    fun updateMusic(music: Music)

    @Delete
    fun deleteMusic(music: Music)

    @Query("DELETE FROM music")
    fun clearTable()

    @Query("SELECT COUNT(Id) FROM music")
    fun getNumberOfMusic():Int

    @Query("SELECT * FROM music WHERE name LIKE :name ")
    fun getMusic(name:String):Music

    @Query("SELECT * FROM music")
    fun getAllMusic(): LiveData<List<Music>>?

}