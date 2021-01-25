package com.example.qode

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.qode.databinding.ActivitySearchBinding
import com.example.qode.register.RegisterActivity

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)

        binding.backToMainButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_down_from_top, R.anim.slide_down_to_bottom)
        }

        var autoTextStrings = arrayOf("C언어", "파이썬", "자바", "printf", "stdio.h", "cout", "java", "python", "studio.h")
        var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, autoTextStrings)
        var autoTextView = findViewById<AutoCompleteTextView>(R.id.mainAutoTextview)
        autoTextView.setAdapter(adapter)
        autoTextView.threshold = 1

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_down_from_top, R.anim.slide_down_to_bottom)
    }

}