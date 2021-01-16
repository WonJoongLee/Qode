package com.example.qode.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qode.R
import com.example.qode.fragment.BaseFragment

class splashFirstFragment : BaseFragment() {
    override fun title(): String {
        return "First Fragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_splash_first, container, false)
        return view
    }
}