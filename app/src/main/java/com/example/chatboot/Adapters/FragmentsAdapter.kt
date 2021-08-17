package com.example.chatboot.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class FragmentsAdapter(manager: FragmentManager)  : FragmentPagerAdapter(manager){

    private val fragList : MutableList<Fragment> = ArrayList()
    private val titleList : MutableList<String> = ArrayList()

    override fun getCount(): Int {
        return fragList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragList.get(position)
    }

    fun addFragment(fragment: Fragment, title: String){
        fragList.add(fragment)
        titleList.add(title)
    }
    override fun getPageTitle(position: Int): CharSequence? {
        return titleList.get(position)
    }
}