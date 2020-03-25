package com.malikane.mussic.Service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import com.malikane.mussic.Enum.PlayerAction
import com.malikane.mussic.State.State

class PlayerBroadCastReceiver:BroadcastReceiver(){
	override fun onReceive(context: Context?, intent: Intent?) {
		when {
			intent!!.action==PlayerAction.ACTION_PLAY.action -> {
				State.PLAYER!!.resumeSong()
			}
			intent.action==PlayerAction.ACTION_PAUSE.action -> {
				State.PLAYER!!.pauseSong()
			}
			intent.action==PlayerAction.ACTION_REPLAY.action -> {
				State.PLAYER!!.playSong()
			}
			intent.action==PlayerAction.ACTION_SHUFFLE_PLAY.action->{
				State.PLAYER!!.shuffleRequest()
			}
			intent.action==Intent.ACTION_HEADSET_PLUG -> {
				State.PLAYER!!.pauseSong()
			}
			intent.action==AudioManager.ACTION_AUDIO_BECOMING_NOISY -> {
				State.PLAYER!!.pauseSong()
			}
		}
	}
}