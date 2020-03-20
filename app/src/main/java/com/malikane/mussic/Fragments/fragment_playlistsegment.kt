package com.malikane.mussic.Fragments

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.malikane.mussic.R

class Fragment_PlaylistSegment:Fragment(){
    private lateinit var viewModel:ViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var text:TextView
    //buraya playlistler için recyler view adapter gelecek
    lateinit var data:SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    var a:Boolean=false
    var b:Boolean=false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v:View=inflater.inflate(R.layout.playlist_page,container,false)
        text=v.findViewById(R.id.deneme)

        return v
    }

    @SuppressLint("CommitPrefEdits")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
       /*data=PreferenceManager.getDefaultSharedPreferences(activity)
        editor=data.edit()
        a=data.getBoolean("isMediaPlayerPlayedSong",a)
        b=data.getBoolean("isServiceConnected",b)
        Log.d("PREFERENCE","111->$a 222->$b")*/

    }
}