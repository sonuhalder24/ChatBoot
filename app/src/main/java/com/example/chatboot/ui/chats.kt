package com.example.chatboot.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatboot.Adapters.FriendsAdapter
import com.example.chatboot.Adapters.FriendsAdapter.OnItemClickedListener
import com.example.chatboot.Models.Friends
import com.example.chatboot.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_chats.*

class chats : Fragment() {

    private lateinit var frList: ArrayList<Friends>
    private lateinit var onItem: OnItemClickedListener
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v:View=inflater.inflate(R.layout.fragment_chats, container, false)
        val recyclerView:RecyclerView=v.findViewById(R.id.recycler)
        frList=ArrayList()
        frList.add(
            Friends("https://firebasestorage.googleapis.com/v0/b/chatboot-98e0e.appspot.com/o/upload_prof_images%2Fbg_prof_img.jpg?alt=media&token=4b02219e-0a7a-4b74-b96a-fdeb461c4e52"
                ,"Dipa Halder","How are you beee?")
        )
        frList.add(
            Friends("https://firebasestorage.googleapis.com/v0/b/chatboot-98e0e.appspot.com/o/upload_prof_images%2Fbg_prof_img.jpg?alt=media&token=4b02219e-0a7a-4b74-b96a-fdeb461c4e52"
                ,"Moyukh Sarkar","How are you beee?")
        )
        frList.add(
            Friends("https://firebasestorage.googleapis.com/v0/b/chatboot-98e0e.appspot.com/o/upload_prof_images%2Fbg_prof_img.jpg?alt=media&token=4b02219e-0a7a-4b74-b96a-fdeb461c4e52"
                ,"Ankita Roy","How are you beee?")
        )
        frList.add(
            Friends("https://firebasestorage.googleapis.com/v0/b/chatboot-98e0e.appspot.com/o/upload_prof_images%2Fbg_prof_img.jpg?alt=media&token=4b02219e-0a7a-4b74-b96a-fdeb461c4e52"
                ,"Saswati Barman","How are you beee?")
        )
        frList.add(
            Friends("https://firebasestorage.googleapis.com/v0/b/chatboot-98e0e.appspot.com/o/upload_prof_images%2Fbg_prof_img.jpg?alt=media&token=4b02219e-0a7a-4b74-b96a-fdeb461c4e52"
                ,"Sambhita Majhi","How are you beee?")
        )
        frList.add(
            Friends("https://firebasestorage.googleapis.com/v0/b/chatboot-98e0e.appspot.com/o/upload_prof_images%2Fbg_prof_img.jpg?alt=media&token=4b02219e-0a7a-4b74-b96a-fdeb461c4e52"
                ,"Dhiman Halder","How are you beee?")
        )
        frList.add(
            Friends("https://firebasestorage.googleapis.com/v0/b/chatboot-98e0e.appspot.com/o/upload_prof_images%2Fbg_prof_img.jpg?alt=media&token=4b02219e-0a7a-4b74-b96a-fdeb461c4e52"
                ,"Rupa Halder","How are you beee?")
        )
        frList.add(
            Friends("https://firebasestorage.googleapis.com/v0/b/chatboot-98e0e.appspot.com/o/upload_prof_images%2Fbg_prof_img.jpg?alt=media&token=4b02219e-0a7a-4b74-b96a-fdeb461c4e52"
                ,"Dipa Halder","How are you beee?")
        )
        frList.add(
            Friends("https://firebasestorage.googleapis.com/v0/b/chatboot-98e0e.appspot.com/o/upload_prof_images%2Fbg_prof_img.jpg?alt=media&token=4b02219e-0a7a-4b74-b96a-fdeb461c4e52"
                ,"Dipa Halder","How are you beee?")
        )
        frList.add(
            Friends("https://firebasestorage.googleapis.com/v0/b/chatboot-98e0e.appspot.com/o/upload_prof_images%2Fbg_prof_img.jpg?alt=media&token=4b02219e-0a7a-4b74-b96a-fdeb461c4e52"
                ,"Dipa Halder","How are you beee?")
        )
        frList.add(
            Friends("https://firebasestorage.googleapis.com/v0/b/chatboot-98e0e.appspot.com/o/upload_prof_images%2Fbg_prof_img.jpg?alt=media&token=4b02219e-0a7a-4b74-b96a-fdeb461c4e52"
                ,"Dipa Halder","How are you beee?")
        )
        frList.add(
            Friends("https://firebasestorage.googleapis.com/v0/b/chatboot-98e0e.appspot.com/o/upload_prof_images%2Fbg_prof_img.jpg?alt=media&token=4b02219e-0a7a-4b74-b96a-fdeb461c4e52"
                ,"Dipa Halder","How are you beee?")
        )
        frList.add(
            Friends("https://firebasestorage.googleapis.com/v0/b/chatboot-98e0e.appspot.com/o/upload_prof_images%2Fbg_prof_img.jpg?alt=media&token=4b02219e-0a7a-4b74-b96a-fdeb461c4e52"
                ,"Dipa Halder","How are you beee?")
        )
        frList.add(
            Friends("https://firebasestorage.googleapis.com/v0/b/chatboot-98e0e.appspot.com/o/upload_prof_images%2Fbg_prof_img.jpg?alt=media&token=4b02219e-0a7a-4b74-b96a-fdeb461c4e52"
                ,"Dipa Halder","How are you beee?")
        )
        frList.add(
            Friends("https://firebasestorage.googleapis.com/v0/b/chatboot-98e0e.appspot.com/o/upload_prof_images%2Fbg_prof_img.jpg?alt=media&token=4b02219e-0a7a-4b74-b96a-fdeb461c4e52"
                ,"Dipa Halder","How are you beee?")
        )

        onItem= object : OnItemClickedListener {
            override fun onItemClicked(pos: Int) {
                Toast.makeText(v.context, pos.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        recyclerView.adapter= FriendsAdapter(v.context,frList,onItem)
        recyclerView.layoutManager=LinearLayoutManager(v.context)
        recyclerView.setHasFixedSize(true)

        return v
    }

}