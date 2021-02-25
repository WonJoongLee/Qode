package com.example.qode.frame

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.RecoverySystem
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.qode.R
import com.example.qode.fragment.BaseFragment
import com.example.qode.recyclerview.QuestionTitleAdapter
import com.example.qode.recyclerview.ShowQuestionData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import kotlin.coroutines.suspendCoroutine

//Viewpager에서 C를 클릭하면 나올 화면.
//여기에 RecyclerView가 들어가야 한다.
class FrameJava : BaseFragment() {

    val mDatas = mutableListOf<ShowQuestionData>()
    var bbsSize : Int = 0

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

        val sharedPreferences: SharedPreferences =activity!!.getSharedPreferences("sharedPrefs",
            AppCompatActivity.MODE_PRIVATE
        )
        val serverString = sharedPreferences.getString("serverUrl", null)
        val boardServerString = serverString.plus("board/")

        val mSwipeRefreshLayout : SwipeRefreshLayout = view.findViewById(R.id.javaSwipeLayout)
        mSwipeRefreshLayout.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener{
            override fun onRefresh() {
                Handler(Looper.getMainLooper()).postDelayed({
                    mSwipeRefreshLayout.isRefreshing = false
                }, 1500) // 1.5초 후 새로고침 멈추는 코드

            }
        })

        GlobalScope.launch {
            getBoardDataFromServer(boardServerString)
        }

//        mDatas.add(ShowQuestionData("Java의 어원이 뭔가요?", "어원이 궁금한데 아시는 분?"))
//        mDatas.add(ShowQuestionData("Java를 잘 잡는 법", "1. 열심히 공부한다. 2. 열심히 공부한다."))
//        mDatas.add(ShowQuestionData("여기서 왜 오류가 나죠?", "책에 있는 데로 따라 했는데 오류가 납니다."))
//        mDatas.add(ShowQuestionData("여기서 왜 오류가 나죠?", "책에 있는 데로 따라 했는데 오류가 납니다."))
//        mDatas.add(ShowQuestionData("여기서 왜 오류가 나죠?", "책에 있는 데로 따라 했는데 오류가 납니다."))
//        mDatas.add(ShowQuestionData("여기서 왜 오류가 나죠?", "책에 있는 데로 따라 했는데 오류가 납니다."))
//        mDatas.add(ShowQuestionData("여기서 왜 오류가 나죠?", "책에 있는 데로 따라 했는데 오류가 납니다."))
//        mDatas.add(ShowQuestionData("여기서 왜 오류가 나죠?", "책에 있는 데로 따라 했는데 오류가 납니다."))
//        mDatas.add(ShowQuestionData("여기서 왜 오류가 나죠?", "책에 있는 데로 따라 했는데 오류가 납니다."))
//        mDatas.add(ShowQuestionData("여기서 왜 오류가 나죠?", "책에 있는 데로 따라 했는데 오류가 납니다."))
//        mDatas.add(ShowQuestionData("여기서 왜 오류가 나죠?", "책에 있는 데로 따라 했는데 오류가 납니다."))
//        mDatas.add(ShowQuestionData("여기서 왜 오류가 나죠?", "책에 있는 데로 따라 했는데 오류가 납니다."))

        adapter.data = mDatas
        adapter.notifyDataSetChanged()
        recyclerView.adapter = adapter

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
                        mDatas.add(ShowQuestionData(j.get("bbsTitle").toString(), j.get("bbsContent").toString(), j.get("created_at").toString(), j.get("border").toString(), j.get("hashtag").toString()))
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

}