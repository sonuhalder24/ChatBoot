package com.example.chatboot

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.chatboot.Adapters.onBoardingItemAdapter
import com.example.chatboot.Models.onBoardingItem

class onBoardingActivity : AppCompatActivity() {
    private lateinit var onBoardingItemAdapter: onBoardingItemAdapter
    private lateinit var indicatorsContainer : LinearLayout

    private lateinit var pref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_on_boarding)

        pref=getSharedPreferences("SHOWN_THIS", Context.MODE_PRIVATE)
        editor=pref.edit()
        if(pref.getBoolean("OPEN",false)){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        setImages()
        setIndicators()
        setCurrentIndicator(0)

    }
    private fun setImages(){
        onBoardingItemAdapter= onBoardingItemAdapter(
            listOf(
                onBoardingItem(
                    anim="chatting.json",
                    title = "Chatting",
                    description = "Chat with your favourite person. Only those person you want to talk, you customize as it is."
                ),
                onBoardingItem(
                    anim= "video_call.json",
                    title = "Phone and Video Call",
                    description = "You can also start video and phone call with your friends."
                ),
                onBoardingItem(
                    anim= "friendship.json",
                    title = "Group Chat and Sharing documents",
                    description = "Group chat facility available and also you share images, pdfs and any documents."
                )

            )
        )
        val onBoardViewPager =findViewById<ViewPager2>(R.id.onBoardingViewPager)
        onBoardViewPager.adapter = onBoardingItemAdapter
        onBoardViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
                if(position>onBoardingItemAdapter.itemCount-2){
                    findViewById<TextView>(R.id.textNxt).setText("Get Started")
                }
                else{
                    findViewById<TextView>(R.id.textNxt).setText("Next")
                }
            }
        })
        (onBoardViewPager.getChildAt(0) as RecyclerView).overScrollMode=
            RecyclerView.OVER_SCROLL_NEVER
        findViewById<LinearLayout>(R.id.nextLay).setOnClickListener {

            if(onBoardViewPager.currentItem<onBoardingItemAdapter.itemCount-2){
                onBoardViewPager.currentItem+=1
                findViewById<TextView>(R.id.textNxt).setText("Next")
            }
            else if(onBoardViewPager.currentItem==onBoardingItemAdapter.itemCount-2){
                onBoardViewPager.currentItem+=1
                findViewById<TextView>(R.id.textNxt).setText("Get Started")
            }
            else if(onBoardViewPager.currentItem==onBoardingItemAdapter.itemCount-1){
                editor.putBoolean("OPEN",true)
                editor.commit()
                startActivity(Intent(applicationContext,MainActivity::class.java))
                finish()
            }
        }
    }

    private fun setIndicators(){
        indicatorsContainer=findViewById<LinearLayout>(R.id.indicatorContainer)
        val indicators= arrayOfNulls<ImageView>(onBoardingItemAdapter.itemCount)
        val layoutParams:LinearLayout.LayoutParams=LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(10,0,10,0)
        for (i in indicators.indices){
            indicators[i]= ImageView(applicationContext)
            indicators[i]?.let {
                it.setImageDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.indicator_inactive_bg))
                it.layoutParams=layoutParams
                indicatorsContainer.addView(it)
            }
        }
    }

    private fun setCurrentIndicator(position: Int){
        val totalChild = indicatorsContainer.childCount
        for(i in 0 until totalChild){
            val img=indicatorsContainer.getChildAt(i) as ImageView
            if(i==position){
                img.setImageDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.indicator_activite_bg))
            }
            else{
                img.setImageDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.indicator_inactive_bg))
            }
        }
    }
}