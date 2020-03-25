package com.malikane.mussic.UI.Fragments

import android.content.*
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
import com.malikane.mussic.UI.ShareBottomSheetDialog
import com.malikane.mussic.ViewModel.MusicViewModel
import java.io.File


class Fragment_Home: Fragment(),View.OnClickListener,
	RecylerViewAdapter.OnItemClickListener,
	RecylerViewAdapter.OptionIconClickListener,
	ShareBottomSheetDialog.ClickListener{

	private lateinit var viewModel:MusicViewModel
	private lateinit var shuffle: Button
	private lateinit var searchBar:EditText
    private lateinit var seekBar: SeekBar
	private lateinit var mediaPlayerContainer: RelativeLayout
	private lateinit var play:Button
	private lateinit var playerText:TextView

    private var Readpermission = ReadPermision()
	//To Update Media Player Progress on UI
	private val handler=Handler()
	//Thread that will update progress of media player while media player playing a song
	private var runnable:Runnable=Runnable {updateProgress()}

	//thread that will check player playing a song and boolean to check whether this thread initialized or not
	private var checkPlayer:Runnable= Runnable {setVisibility()}

	//Thread that will check the saved data whether change
	private var checkDataRunnable:Runnable= Runnable {
		viewModel.getAllMusic()!!.observe(this@Fragment_Home, Observer {
		adapter.setMusicList(it)
	})}

    private lateinit var recyclerview: RecyclerView
    private lateinit var adapter: RecylerViewAdapter

	//if user want to share a music, music name will be store in this variable
	private var sharedMusicName:String? = null

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
		play=v.findViewById(R.id.play_button)
		play.setOnClickListener(this)
		//
		mediaPlayerContainer=v.findViewById(R.id.mediaplayer_container)
		mediaPlayerContainer.visibility=View.GONE
		//
		seekBar=v.findViewById(R.id.player_progress)
		//
		playerText=v.findViewById(R.id.musicName)

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
		adapter.setOptionIconClickListener(this)
		//User already connect with service, these functions already used
		Readpermission.readFile(activity)
		serviceConnection()
		//Check data change or not
		saveMusicInToMusicTable()


		//Start thread and check data whether change
		handler.postDelayed(checkDataRunnable,1000)

		/**
		 * User opened a song on player and change the fragment
		 * then come back to this fragment again, media player played a song.
		 * If media player still playing a song media player has to be visible and
		 * current progress has to be shown
		 * */
		if(State.IS_MEDIA_PLAYER_PLAYED_SONG){
			mediaPlayerContainer.visibility=View.VISIBLE
			if(State.PLAYER!=null)
				setVisibility()
		}
	}

	//RecyclerView Item Click Listener
	override fun OnItemClick(music: Music) {
		startPlay(music.name)
		playerText.text=music.name
	}

	// option item click listener. Option Icon -> Each item's button on recyler view
	override fun onIconClick(music: Music) {
		val bottomSheet = ShareBottomSheetDialog()
		sharedMusicName = music.name
		bottomSheet.setShareButtonClickListener(this)
		bottomSheet.show(fragmentManager!!,"shareBottomSheet")
	}

	override fun onButtonClicked() {
		val musicPATH = "${ Environment.getExternalStorageDirectory().path }/Download/${ sharedMusicName }.mp3"
		val shareIntent = Intent(Intent.ACTION_SEND)
		shareIntent.setType("audio/*")
		shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(musicPATH))
		startActivity(Intent.createChooser(shareIntent,"Share Sound File"))
	}

	override fun onClick(v: View?) {
        when(v!!.id){
            R.id.shuffle->{
				val number=(0..adapter.itemCount).random()
				val music=adapter.getMusicAt(number)
				playerText.text=music.name
				startPlay(music.name)
			}
			R.id.play_button->{
				if(State.PLAYER!!.player.isPlaying){
					State.PLAYER!!.pauseSong()
					play.setBackgroundResource(R.drawable.play_button)
				}
				else{
					if(State.PLAYER!!.player.duration==State.PLAYER!!.player.currentPosition)
					{
						State.PLAYER!!.playSong()
					}
					else{
						State.PLAYER!!.resumeSong()
						setVisibility()
					}
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
				State.PLAYER!!.setListOfMusic(adapter.getAllMusicAtAdapter().map { it.name })//send all music to service when we start the connection
				Log.v("Service Connection","Service were bound")
				State.IS_SERVICE_CONNECTED=true
				setVisibility()
			}

		}
	}

    private fun startPlay(name:String){
		if(!State.IS_SERVICE_CONNECTED){
			State.ACTIVITY=activity
			val intent=Intent(activity,AudioPlayerService::class.java)
			intent.putExtra("NAME",name)
			activity!!.bindService(intent,State.SERVICE_CONNECTION!!,Context.BIND_AUTO_CREATE)
			//
		}
		//Service working
		else{
			State.PLAYER!!.changePath(name)
			setVisibility()
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
			/**
			 * When we changes the song, media player needs a time to be prepare
			 * At this time media player can not play song. Program should check the media player
			 * finish its preparation and start the play new song
			 * */
			handler.postDelayed(checkPlayer,100)
		}

	}
	//Update progress of media player
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
		if(State.IS_SERVICE_CONNECTED) {
			handler.removeCallbacks(runnable)
		}

	}

}


