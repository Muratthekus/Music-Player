package com.malikane.mussic.Database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "artist",
    foreignKeys =
    [
        ForeignKey(entity=Music::class,
            parentColumns= ["Id"],
            childColumns = ["Id"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class Artist(
    @PrimaryKey(autoGenerate = true)
    var Id:Int,
    var name:String="Unknown Artist",
    var songNumber:Int=0)