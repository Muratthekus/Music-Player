package com.malikane.mussic.Service

import android.app.Activity
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.os.Binder
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import androidx.core.app.NotificationCompat
import com.malikane.mussic.Database.Music
import com.malikane.mussic.Enum.PlayerAction
import com.malikane.mussic.Enum.PlayerStatus
import com.malikane.mussic.MainActivity
import com.malikane.mussic.Notificaiton.Notification
import com.malikane.mussic.R
import java.util.*
class AudioPlayerService:Service(),
	MediaPlayer.OnErrorListener,
	MediaPlayer.OnPreparedListener,
	MediaPlayer.OnSeekCompleteListener,
	MediaPlayer.OnCompletionListener,
	AudioManager.OnAudioFocusChangeListener {


	lateinit var player:MediaPlayer

	private var currPos:Int=0
	private var PATH:String?=""
	private var songName:String?=""

	private val iBinder:IBinder=LocalBinder()
	//notification class instance
	private var notification: Notification = Notification(activity = Activity())

	override fun onCreate() {
		super.onCreate()
		PlayerBroadCastReceiver().registerBecomingNoisy()
		PlayerBroadCastReceiver().registerPlayAudio()
	}

	override fun onDestroy() {
		super.onDestroy()
		if(player!=null){
			stopSong()
			player.release()
		}
		notification.deleteNotification()
		PlayerBroadCastReceiver().unRegisterBecomingNoisy()
		PlayerBroadCastReceiver().unRegisterPlayAudio()
	}

	override fun onBind(intent: Intent?): IBinder? {
		PATH= intent?.extras?.getString("PATH",PATH)
		if(!getAudioFocus()){
			Log.d("Media Player Error","CAN NOT GET AUDIO FOCUS")
			stopSelf()
		}
		if(PATH!="")
			initAudioPlayer(PATH)
		return iBinder
	}
	inner class LocalBinder:Binder(){
		fun getService():AudioPlayerService{
			return this@AudioPlayerService
		}

	}
	fun playSong(){
		if(player.isPlaying)
			stopSong()
		player.reset()
		initAudioPlayer(PATH)
	}
	private fun stopSong(){
		if(player.isPlaying)
			player.stop()
	}
	fun pauseSong(){
		if(player.isPlaying)
			player.pause()
		currPos=player.currentPosition
	}
	fun resumeSong(){
		if(!player.isPlaying){
			player.seekTo(currPos)
			//player.start()
		}
	}
	fun changePath(PATH: String?){
		this.PATH=PATH
		playSong()
	}

	// Media Player Methods Begin
	private fun initAudioPlayer(PATH:String?){
		player = MediaPlayer.create(applicationContext, Uri.parse(PATH))
		player.setAudioAttributes(AudioAttributes.Builder()
			.setUsage(AudioAttributes.USAGE_MEDIA)
			.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
		player.setOnCompletionListener(this)
		player.setOnPreparedListener(this)
		player.setOnSeekCompleteListener(this)
		player.setOnErrorListener(this)
	}

	override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
		when(what){
			MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK->{
				Log.e("Media Player Error","NOT VALID FOR PROGRESSIVE PLAYBACK")
			}
			MediaPlayer.MEDIA_ERROR_SERVER_DIED->{
				Log.e("Media Player Error","SERVER DIED")
			}
			MediaPlayer.MEDIA_ERROR_IO->{
				Log.e("Media Player Error","IO ERROR")
			}
			MediaPlayer.MEDIA_ERROR_UNKNOWN->{
				Log.e("Media Player Error","UNKNOWN MEDIA TYPE")
			}
			MediaPlayer.MEDIA_ERROR_UNSUPPORTED->{
				Log.e("Media Player Error","UNSUPPORTED MEDIA TYPE")
			}
		}
		return false
	}

	override fun onSeekComplete(mp: MediaPlayer?) = player.start()

	override fun onPrepared(mp: MediaPlayer?)=player.start()

	override fun onCompletion(mp: MediaPlayer?){
		stopSong()
	}
	private fun getAudioFocus():Boolean{
		val audioManager:AudioManager=getSystemService(Context.AUDIO_SERVICE) as AudioManager
		val request:Int=audioManager.requestAudioFocus(this,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN)
		return request==AudioManager.AUDIOFOCUS_REQUEST_GRANTED
	}

	override fun onAudioFocusChange(focusChange: Int) {
		when(focusChange){
			AudioManager.AUDIOFOCUS_GAIN->{
				playSong()
				player.setVolume(1.0f,1.0f)
			}
			AudioManager.AUDIOFOCUS_LOSS->{
				stopSong()
				player.release()
			}
			AudioManager.AUDIOFOCUS_LOSS_TRANSIENT->pauseSong()

			AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK->player.setVolume(0.3f,0.3f)
		}
	}
	//Media Player Methods End

	inner class PlayerBroadCastReceiver(){
		//Audio output were changes, player has to be stop
		private val becomingNoisy=object: BroadcastReceiver(){
			override fun onReceive(context: Context?, intent: Intent?) {
				pauseSong()
				notification.buildNotification(songName!!,PlayerStatus.STOP)
			}
		}
		//
		fun registerBecomingNoisy(){
			val intentFilter = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
			registerReceiver(becomingNoisy,intentFilter)
		}
		//-----------------------------------------------------------------------
		val playAudio = object : BroadcastReceiver(){
			override fun onReceive(context: Context?, intent: Intent?) {
				playSong()
				notification.buildNotification(songName!!,PlayerStatus.PLAYING)
			}
		}
		fun registerPlayAudio(){
			val intentFilter=IntentFilter(PlayerAction.ACTION_PLAY_NEW.action)
			registerReceiver(playAudio,intentFilter)
		}

		//service gonna die, we have to remove broadcast receiver connection
		fun unRegisterBecomingNoisy(){
			unregisterReceiver(becomingNoisy)
		}
		fun unRegisterPlayAudio(){
			unregisterReceiver(playAudio)
		}

	}


}

