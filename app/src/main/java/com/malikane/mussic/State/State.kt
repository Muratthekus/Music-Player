package com.malikane.mussic.State

import android.content.ServiceConnection
import com.malikane.mussic.Service.AudioPlayerService

class State() {
	/**
	 * This class will keep status of
	 * player
	 * service and
	 * runnable object
	 * @author Muratthekus
	 * */
	companion object Static{
		var IS_MEDIA_PLAYER_PLAYED_SONG = false
		var IS_SERVICE_CONNECTED = false
		var IS_INITIALIZED =false
		var PLAYER:AudioPlayerService?=null
		var SERVICE_CONNECTION:ServiceConnection?=null
	}
}