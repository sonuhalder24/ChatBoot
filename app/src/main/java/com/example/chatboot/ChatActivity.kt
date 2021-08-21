package com.example.chatboot

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager

import com.google.firebase.auth.FirebaseAuth


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.chatboot.Adapters.FragmentsAdapter
import com.example.chatboot.Models.Friends
import com.example.chatboot.ui.chats
import com.example.chatboot.ui.status
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser!!

        if(viewpager!=null){
            setUpViewPager(viewpager)
        }
        tabs.setupWithViewPager(viewpager)

        val menuItemProfile:MenuItem=toolbar.menu.findItem(R.id.profile)
        val menuItemGroup:MenuItem=toolbar.menu.findItem(R.id.groups)

        menuItemProfile.setOnMenuItemClickListener {
            startActivity(Intent(this, Profile::class.java))
            return@setOnMenuItemClickListener true
        }
        menuItemGroup.setOnMenuItemClickListener {
            startActivity(Intent(this, Group::class.java))
            return@setOnMenuItemClickListener true
        }
    }

    private fun setUpViewPager(viewpager: ViewPager) {
        val adapter = FragmentsAdapter(supportFragmentManager)
        adapter.addFragment(chats(), "Chats")
        adapter.addFragment(status(), "Status")
        viewpager.setAdapter(adapter)
    }


    override fun onBackPressed() {
        val intent1=Intent(Intent.ACTION_MAIN)
        intent1.addCategory(Intent.CATEGORY_HOME)
        startActivity(intent1)
    }


}