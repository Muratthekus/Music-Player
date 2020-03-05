package com.malikane.mussic.Service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import java.io.IOException

class MediaPlayerService:Service(),
    MediaPlayer.OnCompletionListener,
    MediaPlayer.OnPreparedListener,
    MediaPlayer.OnErrorListener,
    MediaPlayer.OnSeekCompleteListener,
    MediaPlayer.OnInfoListener,
    MediaPlayer.OnBufferingUpdateListener,
    AudioManager.OnAudioFocusChangeListener{

    lateinit var player:MediaPlayer
    lateinit var PATH:String
    private var pos:Int=0//keep the position of media player cursor
    private val audioManager:AudioManager=getSystemService(Context.AUDIO_SERVICE) as AudioManager

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //Could not get Audio Focus
        if(!getAudioFocus())
            stopSelf()
        if(PATH!="")
            initPlayer()

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSong()
        player.release()
    }
    private fun playSong(){
        if(!player.isPlaying)
            player.start()
    }
    private fun stopSong(){
        if(player.isPlaying)
            player.stop()
    }
    private fun pauseSong(){
        if(player.isPlaying)
            player.pause()
        pos=player.currentPosition
    }
    private fun resumeSong(){
        if(!player.isPlaying){
            player.seekTo(pos)
            player.start()
        }
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        when(what){
            MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK->{
                Log.e("Player Error","NOT VALID FOR PROGRESSIVE PLAYBACK")
            }
            MediaPlayer.MEDIA_ERROR_SERVER_DIED->{
                Log.e("Player Error","SERVER DIED")
            }
            MediaPlayer.MEDIA_ERROR_UNKNOWN->{
                Log.e("Player Error","UNKNOWN ERROR")
            }
        }
        return false
    }

    override fun onCompletion(mp: MediaPlayer?) {
        stopSong()
    }

    override fun onPrepared(mp: MediaPlayer?) {
        playSong()
    }

    override fun onSeekComplete(mp: MediaPlayer?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onInfo(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAudioFocusChange(focusChange: Int) {
        when(focusChange){
            AudioManager.AUDIOFOCUS_GAIN->{
                if(player==null)
                    initPlayer()
                else if(!player.isPlaying)
                    player.start()
                player.setVolume(1f,1f)
            }
            AudioManager.AUDIOFOCUS_LOSS->{
                if(player.isPlaying)
                    player.stop()

                player.release()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT->player.pause()

            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK->player.setVolume(0.3f,0.3f)

        }
    }
    fun getAudioFocus():Boolean {
        val request:Int=audioManager.requestAudioFocus(this,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN)
        return request==AudioManager.AUDIOFOCUS_REQUEST_GRANTED
    }

    //Initialize media player
    private fun initPlayer(){
        player=MediaPlayer()
        player.setOnCompletionListener(this)
        player.setOnErrorListener(this)
        player.setOnPreparedListener(this)
        player.setOnBufferingUpdateListener(this)
        player.setOnSeekCompleteListener(this)
        player.setOnInfoListener(this)
        player.reset()

        player.setAudioStreamType(AudioManager.STREAM_MUSIC)
        try {
        	player.setDataSource(PATH)
        }
        catch (e:IOException){
            Log.e("Service Error","IOException error $e")
        }

    }


}