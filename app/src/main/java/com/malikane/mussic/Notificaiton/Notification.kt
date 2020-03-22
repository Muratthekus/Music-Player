package com.malikane.mussic.Notificaiton

import android.app.Activity
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Color
import android.support.v4.media.MediaMetadataCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.malikane.mussic.Database.Music
import com.malikane.mussic.Enum.PlayerAction
import com.malikane.mussic.Enum.PlayerStatus
import com.malikane.mussic.Fragments.Fragment_Home
import com.malikane.mussic.MainActivity
import com.malikane.mussic.R
import com.malikane.mussic.Service.AudioPlayerService
import com.malikane.mussic.Service.MediaSession

class Notification(private var activity: Activity) {
	//create notification manager to show notification
	private val notificationManager=NotificationManagerCompat.from(activity.applicationContext)
	//we can create multiple notification channel, now we define one channel for our app
	private val NOTIFICATION_ID=308
	fun buildNotification(songName:String,playerStatus: PlayerStatus){
		//when user clicked to content, our app will be open
		val activityIntent=Intent(activity.applicationContext,MainActivity::class.java)
		val contentIntent=PendingIntent.getActivity(activity.applicationContext,0,activityIntent,0)

		val largeIcon=BitmapFactory.decodeResource(Resources.getSystem(),R.drawable.audiotrack_black)
		var notfAction=R.drawable.pause_black
		//Allow to user make some action on notification button
		var playerAction:PendingIntent? = null

		if(playerStatus==PlayerStatus.PLAYING){
			playerAction=playerAction(PlayerAction.ACTION_PAUSE)
		}
		else{
			notfAction=R.drawable.play_button
			playerAction=playerAction(PlayerAction.ACTION_PLAY)
		}
		//create and customize notification
		var builder=NotificationCompat.Builder(activity)
			.setShowWhen(false)
			.setStyle(androidx.media.app.NotificationCompat.MediaStyle()
					//Connect with media session
				.setMediaSession(MediaSession.mediaSession.sessionToken)
					//Show our playback controls in the compat view
				.setShowActionsInCompactView(0,1,2))
			.setLargeIcon(largeIcon)
			.setContentTitle(songName)
			.setContentText("Mussic")
				//When user clicked to content, our app will open
			.setContentIntent(contentIntent)
			.addAction(R.drawable.skip_previous_black,"previous",playerAction(PlayerAction.ACTION_PREV))
			.addAction(notfAction,"pause",playerAction)
			.addAction(R.drawable.skip_next_black,"next",playerAction(PlayerAction.ACTION_NEXT))

			//
			notificationManager.notify(NOTIFICATION_ID,builder.build())
	}

	fun deleteNotification(){
		notificationManager.cancel(NOTIFICATION_ID)
	}

	//create Pending Intent for notification
	fun playerAction(playerAction:PlayerAction):PendingIntent?{
		val intent = Intent(activity,AudioPlayerService::class.java)
		when(playerAction){
			PlayerAction.ACTION_PLAY->intent.action=PlayerAction.ACTION_PLAY.action

			PlayerAction.ACTION_NEXT->intent.action=PlayerAction.ACTION_NEXT.action

			PlayerAction.ACTION_PREV->intent.action=PlayerAction.ACTION_PREV.action

			PlayerAction.ACTION_PAUSE->intent.action=PlayerAction.ACTION_PAUSE.action
		}
		return PendingIntent.getService(activity,0,intent,0)
	}
}