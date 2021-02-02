package com.example.qode.frame

import android.graphics.Insets.add
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.qode.R
import com.example.qode.databinding.FragmentCBinding
import com.example.qode.fragment.BaseFragment
import com.example.qode.recyclerview.QuestionTitleAdapter
import com.example.qode.recyclerview.ShowQuestionData
import org.json.JSONArray
import kotlin.coroutines.suspendCoroutine

//Viewpager에서 C를 클릭하면 나올 화면.
//여기에 RecyclerView가 들어가야 한다.
class FrameC : BaseFragment() {

    private lateinit var binding: FragmentCBinding
    val mDatas = mutableListOf<ShowQuestionData>()
    var bbsSize : Int = 0

    override fun title(): String {
        return "C"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*
        val view = inflater.inflate(R.layout.fragment_c, container, false)
        //view.findViewById<TextView>(R.id.textview).text = "C Lang"
        val mDatas = mutableListOf<ShowQuestionData>()

        val recyclerView = view.findViewById<RecyclerView>(R.id.cRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL ,false)

        val adapter = QuestionTitleAdapter(view.context)

        val mSwipeRefreshLayout : SwipeRefreshLayout = view.findViewById(R.id.cSwipeLayout)
        mSwipeRefreshLayout.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener{
            override fun onRefresh() {
                Handler(Looper.getMainLooper()).postDelayed({
                    mSwipeRefreshLayout.isRefreshing = false
                }, 1500) // 1.5초 후 새로고침 멈추는 코드

            }
        })



        //데이터 추가하는 부분, 추후에 DB랑 연결되면 이 부분 수정
        mDatas.add(ShowQuestionData("printf와 Cout의 차이?", "printf와 Cout의 차이가 궁금합니다.", " ", ","))
        mDatas.add(ShowQuestionData("C언어가 어렵나요?", "코딩 입문자인데 C언어가 어려운지 궁금해요!", " ", " "))
//        mDatas.add(ShowQuestionData("C언어가 어렵나요?", "코딩 입문자인데 C언어가 어려운지 궁금해요!"))
//        mDatas.add(ShowQuestionData("C언어가 어렵나요?", "코딩 입문자인데 C언어가 어려운지 궁금해요!"))
//        mDatas.add(ShowQuestionData("C언어가 어렵나요?", "코딩 입문자인데 C언어가 어려운지 궁금해요!"))
//        mDatas.add(ShowQuestionData("C언어가 어렵나요?", "코딩 입문자인데 C언어가 어려운지 궁금해요!"))
//        mDatas.add(ShowQuestionData("C언어가 어렵나요?", "코딩 입문자인데 C언어가 어려운지 궁금해요!"))
//        mDatas.add(ShowQuestionData("C언어가 어렵나요?", "코딩 입문자인데 C언어가 어려운지 궁금해요!"))

        adapter.data = mDatas // 데이터를 삽입
        adapter.notifyDataSetChanged()
        recyclerView.adapter = adapter
         */
        return view

    }

    private suspend fun getBoardDataFromServer(bsString: String) = suspendCoroutine<String> {
        Handler(Looper.getMainLooper()).postDelayed({
            val request = object : StringRequest(Method.GET, bsString,
                Response.Listener { response ->
                    val jsonArray = JSONArray(response) //  JSONObject가 아니라 array로 바로 오므로 JSONArray로 받아야함
                    bbsSize = jsonArray.length()
                    for(i in 0 until jsonArray.length()){
                        val j = jsonArray.getJSONObject(i)
                        mDatas.add(ShowQuestionData(j.get("bbsTitle").toString(), j.get("bbsContent").toString(), j.get("created_at").toString(), j.get("border").toString()))
                        println("title : ${j.get("bbsTitle")}, boarder : ${j.get("boarder")}")
                        println("content : ${j.get("bbsContent")}, created_at : ${j.get("created_at")}")
                        Log.e("Value", j.toString())
                    }

                }, Response.ErrorListener { error->
                    Log.e("ERROR", error.toString())
                }){
            }
            val queue = Volley.newRequestQueue(activity)
            queue.add(request)
        }, 500)

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