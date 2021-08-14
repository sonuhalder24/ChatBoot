package com.example.chatboot.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.chatboot.Models.onBoardingItem
import com.example.chatboot.R

class onBoardingItemAdapter (private val list: List<onBoardingItem>): RecyclerView.Adapter<onBoardingItemAdapter.OnBoardingItemViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingItemViewHolder {
        val v:View= LayoutInflater.from(parent.context).inflate(R.layout.on_boarding_item_container,parent,false)
        return OnBoardingItemViewHolder(v)
    }

    override fun onBindViewHolder(holder: OnBoardingItemViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class OnBoardingItemViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val animBoard=view.findViewById<LottieAnimationView>(R.id.animOnBoarding)
        private val titleBoard=view.findViewById<TextView>(R.id.textTitle)
        private val descriptionBoard=view.findViewById<TextView>(R.id.textDes)

        fun bind(item: onBoardingItem){
            animBoard.imageAssetsFolder = "images";
            animBoard.setAnimation(item.anim)
            titleBoard.setText(item.title)
            descriptionBoard.setText(item.description)
        }
    }
}