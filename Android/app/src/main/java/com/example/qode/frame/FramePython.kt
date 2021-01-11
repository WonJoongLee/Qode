package com.example.qode.frame

import android.os.Bundle
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
class FramePython : BaseFragment() {
    override fun title(): String {
        return "Python"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_python, container, false)
        val mDatas = mutableListOf<ShowQuestionData>()
        val recyclerView = view.findViewById<RecyclerView>(R.id.pythonRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL ,false)
        val adapter = QuestionTitleAdapter(view.context)

        mDatas.add(ShowQuestionData("Python은 쉽나요?", "제일 쉽다고 들었는데"))
        mDatas.add(ShowQuestionData("print와 printf의 차이가 뭔가요", "궁금합니다."))
        mDatas.add(ShowQuestionData("파이썬으로 뭘 할 수 있나요?", "게임 만들 수 있나요?"))
        mDatas.add(ShowQuestionData("파이썬부터 먼저 공부해도 될까요?", "되냐고"))
        mDatas.add(ShowQuestionData("파이썬부터 먼저 공부해도 될까요?", "되냐고"))
        mDatas.add(ShowQuestionData("파이썬부터 먼저 공부해도 될까요?", "되냐고"))
        mDatas.add(ShowQuestionData("파이썬부터 먼저 공부해도 될까요?", "되냐고"))
        mDatas.add(ShowQuestionData("파이썬부터 먼저 공부해도 될까요?", "되냐고"))
        mDatas.add(ShowQuestionData("파이썬부터 먼저 공부해도 될까요?", "되냐고"))
        mDatas.add(ShowQuestionData("파이썬부터 먼저 공부해도 될까요?", "되냐고"))
        mDatas.add(ShowQuestionData("파이썬부터 먼저 공부해도 될까요?", "되냐고"))
        mDatas.add(ShowQuestionData("파이썬부터 먼저 공부해도 될까요?", "되냐고"))
        mDatas.add(ShowQuestionData("파이썬부터 먼저 공부해도 될까요?", "되냐고"))
        mDatas.add(ShowQuestionData("파이썬부터 먼저 공부해도 될까요?", "되냐고"))

        adapter.data = mDatas
        adapter.notifyDataSetChanged()
        recyclerView.adapter = adapter
        return view
    }

}