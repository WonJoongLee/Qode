package com.example.qode.splash

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.qode.fragment.BaseFragment

class SplashAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT ) {
    override fun getCount() = 3

    override fun getItem(position: Int): Fragment {
        return when(position){
            0->splashFirstFragment()
            1->splashSecondFragment()
            2->splashThirdFragment()
            else -> splashSecondFragment()
        }
    }
}