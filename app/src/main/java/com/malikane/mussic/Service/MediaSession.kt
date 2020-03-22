package com.malikane.mussic.Service

import android.app.Service
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat

class MediaSession {
	companion object Static{
		lateinit var mediaSessionManager:MediaSessionManager
		lateinit var mediaSession:MediaSessionCompat
		lateinit var transportControls: MediaControllerCompat.TransportControls
	}
	private val NOTIFICATION_ID=101
	fun initMediaSession(){
	}

}