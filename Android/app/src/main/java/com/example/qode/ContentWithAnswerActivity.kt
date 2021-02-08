package com.example.qode

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qode.databinding.ActivityContentWithAnswerBinding
import com.example.qode.recyclerview.AnswerAdapter
import com.example.qode.recyclerview.AnswerData

class ContentWithAnswerActivity : AppCompatActivity() {
    private lateinit var binding : ActivityContentWithAnswerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_content_with_answer)

        val answerDataList = mutableListOf<AnswerData>()
        val answerRecyclerView = findViewById<RecyclerView>(R.id.answerRecyclerView)
        val adapter = AnswerAdapter(applicationContext)
        answerRecyclerView.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        answerDataList.add(AnswerData("가장 큰 차이라면 cout은 출력하려는 값의 형(type)을 컴파일러가 검사할 수 있는 반면에 printf()는 그렇지 못하다는 것입니다. \n", "홍길동", "1", "2"))
        answerDataList.add(AnswerData("Kotlin은 세계 최고의 상용 개발 도", "강태공", "3", " -2"))
        answerDataList.add(AnswerData("2018년 카카오가 카카오톡 메시징 서버에 Kotlin을 사용해 본 결과, 코드량이 비약적으로 감소하고 생산성이 대폭 향상되었다고 한다.", "오카카", "0", " 1"))
        answerDataList.add(AnswerData("장황했던 Java와 비교하면 눈물날 정도로 간결한 문법을 제공한다. 간결한 문법을 제공하면서도 런타임 오버헤드가 거의 없다.", "네이버", "4", " 2"))
        answerDataList.add(AnswerData("예외처리를 강제하지 않는다. JSONObject를 만들 때 눈물이 난다...", "오바마", "0", " 2"))
        answerRecyclerView.adapter = adapter
        answerRecyclerView.setHasFixedSize(false) // ?
        adapter.data = answerDataList
        adapter.notifyDataSetChanged()
    }
}