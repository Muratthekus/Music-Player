package com.malikane.mussic.Fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.*
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.postDelayed
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
import com.malikane.mussic.State.State
import com.malikane.mussic.ViewModel.MusicViewModel
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates


class Fragment_Home: Fragment(),View.OnClickListener,RecylerViewAdapter.OnItemClickListener{

	private lateinit var viewModel:MusicViewModel
	private lateinit var shuffle: Button
	private lateinit var searchBar:EditText
    private lateinit var seekBar: SeekBar
	private lateinit var mediaPlayerContainer: RelativeLayout
	private lateinit var play:Button

	private var musics:List<Music> = ArrayList()
    private var Readpermission = ReadPermision()
	//To Update Media Player Progress on UI
	private val handler=Handler()
	//Thread that will update progress of media player while media player playing a song
	private var runnable:Runnable=Runnable {updateProgress()}

	//thread that will check player playing a song and boolean to check whether this thread initialized or not
	private var checkPlayer:Runnable= Runnable {setVisibility()}


	lateinit var data:SharedPreferences
	lateinit var editor:SharedPreferences.Editor
	private lateinit var PATH: String
    private lateinit var recyclerview: RecyclerView
    private lateinit var adapter: RecylerViewAdapter

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
		//
		//
		mediaPlayerContainer=v.findViewById(R.id.mediaplayer_container)
		mediaPlayerContainer.visibility=View.GONE
		//
		seekBar=v.findViewById(R.id.player_progress)

		play=v.findViewById(R.id.play)
		play.setOnClickListener(this)

		//define shared preference

        return v
    }

	override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel=ViewModelProviders.of(this).get(MusicViewModel::class.java)
        //define recycler view adapter
        adapter= RecylerViewAdapter()
        recyclerview.adapter=adapter
        adapter.SetOnItemClickListener(this)
		PATH = Environment.getExternalStorageDirectory().path+"/Download/"
		//User already connect with service, these functions already used
		Readpermission.readFile(activity)
		serviceConnection()
		//Check data change or not
		saveMusicInToMusicTable()
		CheckData().run()
		if(State.IS_MEDIA_PLAYER_PLAYED_SONG){
			mediaPlayerContainer.visibility=View.VISIBLE
			if(State.PLAYER!=null)
				setVisibility()
		}
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
			R.id.play->{
				if(State.PLAYER!!.player.isPlaying){
					State.PLAYER!!.pauseSong()
					play.setBackgroundResource(R.drawable.play_button)
				}
				else{
					if(State.PLAYER!!.player.duration==State.PLAYER!!.player.currentPosition)
						startPlay(PATH)
					else
						State.PLAYER!!.resumeSong()
				}
			}
        }
    }

	private fun serviceConnection(){
		State.SERVICE_CONNECTION = object :
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
				State.PLAYER = binder.getService()
				Log.v("Service Connection","Service were bound")
				State.IS_SERVICE_CONNECTED=true
				setVisibility()
			}

		}
	}

    private fun startPlay(PATH:String){
		if(!State.IS_SERVICE_CONNECTED){
			val intent=Intent(activity,AudioPlayerService::class.java)
			intent.putExtra("PATH",PATH)
			activity!!.bindService(intent,State.SERVICE_CONNECTION!!,Context.BIND_AUTO_CREATE)
			//
		}
		//Service working
		else{
			//handler.removeCallbacks(runnable)
			State.PLAYER!!.changePath(PATH)
			setVisibility()
			//handler.postDelayed(runnable,100)
		}

    }

	private fun setVisibility(){
		if(State.PLAYER!!.player.isPlaying){
			//save true to static member of state class
			State.IS_MEDIA_PLAYER_PLAYED_SONG=true
			mediaPlayerContainer.visibility=View.VISIBLE
			play.setBackgroundResource(R.drawable.pause_black)
			seekBar.max=State.PLAYER!!.player.duration
			seekBar.progress=0
			if(State.IS_INITIALIZED)
				handler.removeCallbacks(checkPlayer)//player now start to play, we don't need to check
			updateProgress()
		}
		else{
			play.setBackgroundResource(R.drawable.play_button)
			State.IS_INITIALIZED=true
			//function will call itself until player start to play
			handler.postDelayed(checkPlayer,100)
		}

	}

	private fun updateProgress(){
		seekBar.progress=State.PLAYER!!.player.currentPosition
		if(State.PLAYER!!.player.isPlaying){
			handler.postDelayed(runnable,100)
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

	inner class CheckData():Runnable{
		override fun run() {
			viewModel.getAllMusic()!!.observe(this@Fragment_Home, Observer {
				adapter.setMusicList(it)
			})
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		if(State.IS_SERVICE_CONNECTED) {
			//State.IS_SERVICE_CONNECTED=false
			//activity!!.unbindService(serviceConnection)
			handler.removeCallbacks(runnable)
			//player.stopSelf()
		}

	}



}


