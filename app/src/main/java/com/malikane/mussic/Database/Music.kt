package com.malikane.mussic.Database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "music")
data class Music(
    @PrimaryKey(autoGenerate = true)
    var Id:Int=0,
    var artistName:String,
    var name:String)

