package com.malikane.mussic.Database

import android.content.Context
import androidx.room.Room
import androidx.room.Database
import androidx.room.RoomDatabase
import com.malikane.mussic.Database.Dao.Music2PlaylistDao
import com.malikane.mussic.Database.Dao.MusicDao
import com.malikane.mussic.Database.Dao.PlaylistDao


@Database(entities = [Music::class,PlaylistBase::class,Music2Playlist::class],version = 5)
abstract class MussicDatabase:RoomDatabase(){
    abstract fun musicDao():MusicDao
    abstract fun mussic2playlistDao():Music2PlaylistDao
    abstract fun playlistDao():PlaylistDao

    companion object statics{
                private var INSTANCE:MussicDatabase?=null
                fun getAppDataBase(context: Context):MussicDatabase?{
                    if(INSTANCE==null){
                        synchronized(MussicDatabase::class){
                            INSTANCE=Room.databaseBuilder(context.applicationContext,MussicDatabase::class.java,"myDB")
                                .fallbackToDestructiveMigration().build()
                        }
                    }
                    return INSTANCE
        }
        fun DestroyDataBase(){
            INSTANCE=null
        }
    }
}