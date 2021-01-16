package com.example.qode.splash

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.qode.MainActivity
import com.example.qode.R
import com.example.qode.fragment.BaseFragment

class splashThirdFragment : BaseFragment() {
    override fun title(): String {
        return "Second Fragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_splash_third, container, false)
        var button = view.findViewById<Button>(R.id.buttonMoveToMain)
        button.setOnClickListener {
            val intent = Intent(view.context, MainActivity::class.java)
            startActivity(intent)
        }
        return view
    }
}