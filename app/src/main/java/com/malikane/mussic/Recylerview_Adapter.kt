package com.malikane.mussic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.malikane.mussic.Database.Music

class RecylerViewAdapter : RecyclerView.Adapter<RecylerViewAdapter.MusicHolder>(){
    private var musics:List<Music> = ArrayList()
    lateinit var listener: OnItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicHolder {
        val itemview: View = LayoutInflater.from(parent.context).inflate(R.layout.music_recylerview,parent,false)

        return MusicHolder(itemview)

    }
    override fun onBindViewHolder(holder: MusicHolder, position: Int) {
        val music:Music= musics[position]
        holder.songName.text = music.name
        holder.artistName.text=music.artistName
        holder.itemView.setOnClickListener {
            val pos = holder.adapterPosition
            if(pos!=RecyclerView.NO_POSITION){
                listener.OnItemClick(music)
            }
        }
    }
    class MusicHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var songName: TextView=itemView.findViewById(R.id.songName)
        var artistName: TextView=itemView.findViewById(R.id.artistname)
    }

    override fun getItemCount(): Int {
        return musics.size
    }
    fun setMusicList(musics:List<Music>){
        this.musics=musics
        notifyDataSetChanged()
    }
    fun getMusicAt(position: Int):Music{
        return musics[position]
    }
    interface OnItemClickListener{
        fun OnItemClick(music:Music)
    }
    fun SetOnItemClickListener(listener:OnItemClickListener){
        this.listener=listener
    }


}