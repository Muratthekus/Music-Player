package com.malikane.mussic.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlistBase")
data class PlaylistBase(
    @PrimaryKey(autoGenerate = true)
    var Id:Int=0,
    var name:String,
    var description:String,
    var size:Int=0,
    var duration:Int=0)