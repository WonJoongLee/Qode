package com.example.qode.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.example.qode.MainActivity
import com.example.qode.R
import com.example.qode.databinding.ActivitySplashScreenBinding
import com.example.qode.databinding.ActivitySplashScreenBindingImpl
import com.example.qode.preferences.SharedData.Companion.prefs

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)

        val adapter = SplashAdapter(supportFragmentManager)
        binding.splashPager.adapter = adapter

        binding.splashPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                binding.ciMainActivity.selectDot(position) // Page가 변경되면 indicator 변경
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })

        binding.ciMainActivity.createDotPanel(3, R.drawable.indicator_dot_off, R.drawable.indicator_dot_on, 0)

        if(prefs.getFirstTime("firstTime", "").isEmpty()){ // 만약 첫 접속이라면
            prefs.setFirstTime("firstTime","false")//이후에는 첫 접속이 아니므로 firstTime을 false로 설정해준다.

        }else{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }

    }
}