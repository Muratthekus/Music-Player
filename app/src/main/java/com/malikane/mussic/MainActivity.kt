package com.malikane.mussic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import me.ibrahimsn.lib.OnItemSelectedListener
import me.ibrahimsn.lib.SmoothBottomBar

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomBar:SmoothBottomBar=findViewById(R.id.bottomBar)
        var fragment:Fragment=Fragment_Home()
        supportFragmentManager.beginTransaction().replace(R.id.framelayout,fragment).commit()
        bottomBar.setOnItemSelectedListener(object:OnItemSelectedListener{
            override fun onItemSelect(pos: Int) {
                if(pos==0){
                    fragment=Fragment_Home()
                }
                else if(pos==1){
                    fragment=Fragment_PlaylistSegment()
                }
                supportFragmentManager.beginTransaction().replace(R.id.framelayout,fragment).commit()
            }
        })
    }
}
