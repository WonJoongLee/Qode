package com.example.qode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.qode.databinding.ActivityMainBinding
import com.example.qode.fragment.SamplePagerAdapter
//import com.example.qode.fragment.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var titleList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //postToList()
        //binding.pager.adapter = ViewPagerAdapter(titleList)
        //binding.pager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val adapter2 = SamplePagerAdapter(supportFragmentManager)
        binding.pager.adapter = adapter2
        binding.tabLayout.setupWithViewPager(binding.pager)

/*
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            //tab.text = "Tab ${position + 1}"
            when(position){
                0 -> tab.text = "C"
                1 -> tab.text = "Java"
                2 -> tab.text = "Python"
            }
        }.attach()
        */

        //TabLayout Listener
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) { // Tab이 클릭 됐을 때
                Toast.makeText(this@MainActivity, "Selected ${tab?.text}", Toast.LENGTH_SHORT).show()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) { // 클릭 해제 됐을 때
                Toast.makeText(this@MainActivity, "UnSelected ${tab?.text}", Toast.LENGTH_SHORT).show()
            }

            override fun onTabReselected(tab: TabLayout.Tab?) { // 같은 탭을 다시 클릭 했을 때
                Toast.makeText(this@MainActivity, "ReSelected ${tab?.text}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    //addToList와 postToList는 아이템 추가하는 것것
   private fun addToList(title:String){
        titleList.add(title)
    }

    private fun postToList(){
        for(i in 1..3){
            addToList("Title $i")
        }
    }
}

