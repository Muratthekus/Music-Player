package com.malikane.mussic.Database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "music",
    foreignKeys =
    [
        ForeignKey(entity=Artist::class,
        parentColumns= ["Id"],
        childColumns = ["artistId"],
        onDelete =CASCADE)

    ])
data class Music(
    @PrimaryKey(autoGenerate = true)
    var Id:Int,
    var artistId:Int,
    var artistName:String,
    var name:String,
    var duration:Int)

