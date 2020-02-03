package com.malikane.mussic

import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malikane.mussic.ViewModel.MusicViewModel

class Fragment_Home: Fragment(){
    private lateinit var viewModel:ViewModel
    //val PATH: String=getFilesDir()
    lateinit var recylerview: RecyclerView
    lateinit var adapter:RecylerViewAdapter
    //static objects
    companion object statisc{
        lateinit var data:SharedPreferences
        lateinit var editor:SharedPreferences.Editor
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v:View=inflater.inflate(R.layout.home__page,container,false)
        recylerview=v.findViewById(R.id.recylerView)
        recylerview.layoutManager=LinearLayoutManager(activity)
        recylerview.setHasFixedSize(true)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel=ViewModelProviders.of(this).get(MusicViewModel::class.java)
        adapter= RecylerViewAdapter()
        recylerview.adapter=adapter
        data=PreferenceManager.getDefaultSharedPreferences(activity?.application?.applicationContext)
        editor=data.edit()

    }

    override fun onResume() {
        super.onResume()

    }
    fun fetchMusic(){

    }
    private class FetchMusicAsyncTask:AsyncTask<Void,Void,Void>(){
        override fun doInBackground(vararg params: Void?): Void? {
            //TO-DO
            return null
        }

    }
}