package com.malikane.mussic.Permission

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class ReadPermision{

	//Get permission for reading file at device
	fun readFile(activity: Activity?){
		if (ContextCompat.checkSelfPermission(activity!!.applicationContext,
				Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {

			// Permission is not granted
			// Should we show an explanation?
			if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
				// Show an explanation to the user *asynchronously* -- don't block
				// this thread waiting for the user's response! After the user
				// sees the explanation, try again to request the permission.
			} else {
				// No explanation needed, we can request the permission.
				ActivityCompat.requestPermissions(activity,
					arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),101)

				// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
				// app-defined int constant. The callback method gets the
				// result of the request.
			}
		}
	}


}
