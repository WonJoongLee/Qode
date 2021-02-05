package com.example.qode

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.qode.databinding.ActivityContentBinding

class ContentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_content)
        val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE)

        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        val createdTime = intent.getStringExtra("createdTime")
        val boardNum = intent.getStringExtra("boardNum")
        val serverString = sharedPreferences.getString("serverUrl", null)
        //val boardCommentUrl = serverString.plus("comment/").plus("1") // TODO comment는 아직 들어있는 값이 없어 추후 구현

        binding.title.text = title
        binding.content.text = content
        binding.createdTime.text = createdTime
    }
}