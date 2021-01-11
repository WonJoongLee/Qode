package com.example.qode.frame

import android.os.Bundle
import android.os.RecoverySystem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qode.R
import com.example.qode.fragment.BaseFragment
import com.example.qode.recyclerview.QuestionTitleAdapter
import com.example.qode.recyclerview.ShowQuestionData

//Viewpager에서 C를 클릭하면 나올 화면.
//여기에 RecyclerView가 들어가야 한다.
class FrameJava : BaseFragment() {
    override fun title(): String {
        return "Java"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_java, container, false)
        val mDatas = mutableListOf<ShowQuestionData>()
        val recyclerView = view.findViewById<RecyclerView>(R.id.javaRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL ,false)
        val adapter = QuestionTitleAdapter(view.context)

        mDatas.add(ShowQuestionData("Java의 어원이 뭔가요?", "어원이 궁금한데 아시는 분?"))
        mDatas.add(ShowQuestionData("Java를 잘 잡는 법", "1. 열심히 공부한다. 2. 열심히 공부한다."))
        mDatas.add(ShowQuestionData("여기서 왜 오류가 나죠?", "책에 있는 데로 따라 했는데 오류가 납니다."))
        mDatas.add(ShowQuestionData("여기서 왜 오류가 나죠?", "책에 있는 데로 따라 했는데 오류가 납니다."))
        mDatas.add(ShowQuestionData("여기서 왜 오류가 나죠?", "책에 있는 데로 따라 했는데 오류가 납니다."))
        mDatas.add(ShowQuestionData("여기서 왜 오류가 나죠?", "책에 있는 데로 따라 했는데 오류가 납니다."))
        mDatas.add(ShowQuestionData("여기서 왜 오류가 나죠?", "책에 있는 데로 따라 했는데 오류가 납니다."))
        mDatas.add(ShowQuestionData("여기서 왜 오류가 나죠?", "책에 있는 데로 따라 했는데 오류가 납니다."))
        mDatas.add(ShowQuestionData("여기서 왜 오류가 나죠?", "책에 있는 데로 따라 했는데 오류가 납니다."))
        mDatas.add(ShowQuestionData("여기서 왜 오류가 나죠?", "책에 있는 데로 따라 했는데 오류가 납니다."))
        mDatas.add(ShowQuestionData("여기서 왜 오류가 나죠?", "책에 있는 데로 따라 했는데 오류가 납니다."))
        mDatas.add(ShowQuestionData("여기서 왜 오류가 나죠?", "책에 있는 데로 따라 했는데 오류가 납니다."))

        adapter.data = mDatas
        adapter.notifyDataSetChanged()
        recyclerView.adapter = adapter

        return view
    }

}