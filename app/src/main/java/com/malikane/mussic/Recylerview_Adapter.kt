package com.malikane.mussic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malikane.mussic.Database.Music

class RecylerViewAdapter : RecyclerView.Adapter<RecylerViewAdapter.MusicHolder>() {
    private var musics:List<Music> = ArrayList()
    lateinit var listener: OnItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicHolder {
        val itemview: View = LayoutInflater.from(parent.context).inflate(R.layout.music_recylerview,parent,false)
        return MusicHolder(itemview)
    }
    override fun onBindViewHolder(holder: MusicHolder, position: Int) {
        val music:Music= musics[position]
        holder.songName.text = music.name
        holder.artistName.text = music.artistName
        holder.duration.text=music.duration.toString()
        holder.genre.text = "-"
    }
    class MusicHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var songName: TextView=itemView.findViewById(R.id.songName)
        var artistName: TextView=itemView.findViewById(R.id.artistname)
        var genre: TextView=itemView.findViewById(R.id.genre)
        var duration: TextView=itemView.findViewById(R.id.duration)
    }
    override fun getItemCount(): Int {
        return musics.size
    }
    fun setPlaylist(music:List<Music>){
        this.musics=music
        notifyDataSetChanged()
    }

    fun getNoteAt(position: Int):Music{
        return musics[position]
    }
    interface OnItemClickListener{
        fun OnItemClick(music:Music)
    }
    fun SetOnItemClickListener(listener:OnItemClickListener){
        this.listener=listener
    }


}