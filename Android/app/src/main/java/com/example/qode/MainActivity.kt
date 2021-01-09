package com.example.qode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.qode.databinding.ActivityMainBinding
import com.example.qode.fragment.SamplePagerAdapter
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val adapter2 = SamplePagerAdapter(supportFragmentManager)
        binding.pager.adapter = adapter2
        binding.tabLayout.setupWithViewPager(binding.pager)

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

        binding.fab.setOnClickListener{
            Toast.makeText(this, "클릭 감지 완료", Toast.LENGTH_SHORT).show()
        }
        binding.hamburgerImageButton.setOnClickListener {
            Toast.makeText(this, "클릭 감지 완료", Toast.LENGTH_SHORT).show()
        }
    }
}

