package com.example.qode.frame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.qode.R
import com.example.qode.fragment.BaseFragment

//Viewpager에서 C를 클릭하면 나올 화면.
//여기에 RecyclerView가 들어가야 한다.
class FramePython : BaseFragment() {
    override fun title(): String {
        return "Python"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_c, container, false)
        view.findViewById<TextView>(R.id.textview).text = "Python Lang"
        return view
    }

    /*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_c, null)
        //RecyclerView 작업해주기
        return view
    }
     */
}