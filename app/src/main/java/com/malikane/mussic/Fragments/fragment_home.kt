package com.malikane.mussic.Fragments

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.os.IBinder
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malikane.mussic.Database.Music
import com.malikane.mussic.Permission.ReadPermision
import com.malikane.mussic.R
import com.malikane.mussic.RecylerViewAdapter
import com.malikane.mussic.Service.AudioPlayerService
import com.malikane.mussic.ViewModel.MusicViewModel
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class Fragment_Home: Fragment(),View.OnClickListener,RecylerViewAdapter.OnItemClickListener{
    private lateinit var viewModel:MusicViewModel
    var isServiceConnected:Boolean = false
    private lateinit var shuffle: Button
	private lateinit var searchBar:EditText
    private var musics:List<Music> = ArrayList()
    private var Readpermission = ReadPermision()

    private lateinit var PATH: String
    private lateinit var serviceConnection: ServiceConnection
    private lateinit var recyclerview: RecyclerView
    private lateinit var adapter: RecylerViewAdapter
    lateinit var player:AudioPlayerService
    //static objects
    companion object Statics{
        lateinit var data:SharedPreferences
        lateinit var editor:SharedPreferences.Editor
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v:View=inflater.inflate(R.layout.home__page,container,false)
        recyclerview=v.findViewById(R.id.recylerView)
        recyclerview.layoutManager=LinearLayoutManager(activity)
        recyclerview.setHasFixedSize(true)
        //
		searchBar = v.findViewById(R.id.searchBar)
		//
        shuffle=v.findViewById(R.id.shuffle)
		shuffle.setOnClickListener(this)
		//define shared preference
		data = PreferenceManager.getDefaultSharedPreferences(activity?.applicationContext)
		editor = data.edit()
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

		data =PreferenceManager.getDefaultSharedPreferences(activity?.application?.applicationContext)
		editor = data.edit()

        viewModel=ViewModelProviders.of(this).get(MusicViewModel::class.java)
        //define recycler view adapter
        adapter= RecylerViewAdapter()
        recyclerview.adapter=adapter
        adapter.SetOnItemClickListener(this)
		PATH = Environment.getExternalStorageDirectory().path+"/Download/"

		//Check Permission
		Readpermission.readFile(activity)

		serviceConnection()
		//Check data change or not
		CheckData().run()


        saveMusicInToMusicTable()

	}

	//RecyclerView Item Click Listener
	override fun OnItemClick(music: Music) {
		val absPath=PATH+music.name+".mp3"
		startPlay(absPath)
	}

	override fun onClick(v: View?) {
        when(v!!.id){
            R.id.shuffle->{
				val flag=viewModel.clearTable()
                Toast.makeText(activity!!.applicationContext,"Operation $flag",Toast.LENGTH_LONG).show()
				Log.d("MUSIC NUMBER -1","${viewModel.getNumberOfMusic()}")
			}
        }

    }

	private fun serviceConnection(){
		serviceConnection = object :
			ServiceConnection {

			override fun onServiceDisconnected(name: ComponentName?) {
				Log.v("Service Connection","Service not bound")
			}

			override fun onServiceConnected(
				name: ComponentName,
				service: IBinder
			) {
				val binder: AudioPlayerService.LocalBinder =
					service as AudioPlayerService.LocalBinder
				player = binder.getService()
				isServiceConnected=true
				Log.v("Service Connection","Service were bound")
			}

		}
	}

    private fun startPlay(PATH:String){
		if(!isServiceConnected){
			val intent=Intent(activity,AudioPlayerService::class.java)
			intent.putExtra("PATH",PATH)
			activity!!.bindService(intent,serviceConnection,Context.BIND_AUTO_CREATE)
		}
		//Service working
		else{
			player.changePath(PATH)
		}

    }

    private fun saveMusicInToMusicTable(){
        /**
         * There is no entity in music table, fetch music from file
         * and save it into music table
         * */
		//There is no item in adapter, fetch musics

		if(viewModel.getNumberOfMusic()==0){
			val path=Environment.getExternalStorageDirectory().path+"/Download/"
			val directory=File(path)
			val files= arrayOf<File>(*directory.listFiles())

			for(file in files){
				if(fileTypeChecker(file)!=null){
                    val fileName=file.name.subSequence(0,file.name.length-4) // For not take file type `song.mp3` -> `song`
                    val music=Music(artistName ="Unknown Artist",name = fileName.toString())

                    viewModel.addSong(music)
                }
			}
        }
		Log.d("ENTITY","${viewModel.getNumberOfMusic()}")
    }

    //Check file type is mp3 or not, if mp3 return file
    private fun fileTypeChecker(file:File):File?{
        /**
         * @author Muratthekus
        * @return Return file that in mp3 format
        * */
        val typeOfFile= file.name.split(".")
        if(typeOfFile[typeOfFile.lastIndex] == "mp3")
            return file
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        if(isServiceConnected){
            activity!!.unbindService(serviceConnection)
            player.stopSelf()
        }
    }

	inner class CheckData():Runnable{
		override fun run() {
			viewModel.getAllMusic()!!.observe(this@Fragment_Home, Observer {
				adapter.setMusicList(it)
				searchBar.setText(adapter.itemCount.toString())
			})

		}
	}


}


