package com.example.qode.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.qode.frame.FrameC
import com.example.qode.frame.FrameJava
import com.example.qode.frame.FramePython

class FrameAdapter : FragmentPagerAdapter {

    private val list : ArrayList<BaseFragment> = ArrayList();

    constructor(fragmentManager: FragmentManager) : super(fragmentManager){
        list.add(FrameC())
        list.add(FrameJava())
        list.add(FramePython())
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return list[position].title()
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {
        return list.get(position)
    }
}