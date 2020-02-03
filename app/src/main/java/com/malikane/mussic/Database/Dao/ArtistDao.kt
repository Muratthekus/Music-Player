package com.malikane.mussic.Database.Dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.malikane.mussic.Database.Artist
import com.malikane.mussic.Database.Music

@Dao
interface ArtistDao {
    @Insert
    fun addArtist(artist: Artist)

    @Update
    fun updateArtist(artist: Artist)

    @Delete
    fun deleteArtist(artist: Artist)

    @Query("SELECT * FROM artist")
    fun getAllArtist(): LiveData<List<Artist>>

    @Query("SELECT * FROM music INNER JOIN artist ON music.artistId=artist.Id WHERE artist.Id=:artistId")
    fun getArtistSong(artistId:Int):LiveData<List<Music>>


}