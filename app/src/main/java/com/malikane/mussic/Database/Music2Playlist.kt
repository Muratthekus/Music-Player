package com.malikane.mussic.Database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "music2playlist")
data class Music2Playlist(
    @PrimaryKey(autoGenerate = true)
    var tableId:Int,
    var musicId:Int,
    var playlistId:Int)