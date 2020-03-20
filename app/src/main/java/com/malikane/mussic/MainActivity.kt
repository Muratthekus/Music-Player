package com.malikane.mussic

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import com.malikane.mussic.Fragments.Fragment_Home
import com.malikane.mussic.Fragments.Fragment_PlaylistSegment
import me.ibrahimsn.lib.OnItemSelectedListener
import me.ibrahimsn.lib.SmoothBottomBar

class MainActivity : AppCompatActivity() {


    lateinit var fragment:Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomBar:SmoothBottomBar=findViewById(R.id.bottomBar)
        fragment = Fragment_Home()
        supportFragmentManager.beginTransaction().replace(R.id.framelayout,fragment).commit()

        bottomBar.setOnItemSelectedListener(object:OnItemSelectedListener{
            override fun onItemSelect(pos: Int) {
                if(pos==0){
                    fragment = Fragment_Home()
                }
                else if(pos==1){
                    fragment = Fragment_PlaylistSegment()
                }
                supportFragmentManager.beginTransaction().replace(R.id.framelayout,fragment).commit()
            }
        })
    }



}
