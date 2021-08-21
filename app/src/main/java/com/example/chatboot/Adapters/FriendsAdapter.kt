package com.example.chatboot.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatboot.Models.Friends
import com.example.chatboot.R
import kotlinx.android.synthetic.main.show_users.view.*

class FriendsAdapter(private val c: Context,private val list:ArrayList<Friends>,private val listener:OnItemClickedListener) : RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder>(){
    interface OnItemClickedListener{
        fun onItemClicked(pos:Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        val views:View=LayoutInflater.from(c).inflate(R.layout.show_users,parent,false)
        return FriendsViewHolder(views)
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        holder.nameFriend.text = list.get(position).displayName
        holder.lastMsgFriend.text = list.get(position).lastMsg
        Glide.with(c).load(list.get(position).imageUrl).into(holder.imageFriend)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class FriendsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageFriend:ImageView=itemView.friImg
        val nameFriend:TextView=itemView.userName
        val lastMsgFriend:TextView=itemView.userChat
        val vHr:View=itemView.vHr

        init {
            itemView.setOnClickListener {
                if(adapterPosition!=RecyclerView.NO_POSITION) {
                    listener.onItemClicked(adapterPosition)
                }
            }
        }
    }

}