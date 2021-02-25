package com.example.qode

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.qode.databinding.ActivitySearchBinding
import com.example.qode.recyclerview.QuestionTitleAdapter
import com.example.qode.recyclerview.ShowQuestionData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import kotlin.coroutines.suspendCoroutine


class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    val searchList = mutableListOf<ShowQuestionData>()
    private var idArr = mutableListOf<Int>()
    lateinit var searchRV : RecyclerView //= findViewById<RecyclerView>(R.id.searchRecyclerView)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        var searchRVAdapter : QuestionTitleAdapter = QuestionTitleAdapter(applicationContext, idArr)

        val serverUrl =
            getSharedPreferences("sharedPrefs", MODE_PRIVATE).getString("serverUrl", null)

        binding.backToMainButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_down_from_top, R.anim.slide_down_to_bottom)
        }

        val autoTextStrings =
            arrayOf("C언어", "파이썬", "자바", "printf", "stdio.h", "cout", "java", "python", "studio.h")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, autoTextStrings)
        val autoTextView = findViewById<AutoCompleteTextView>(R.id.mainAutoTextview)
        autoTextView.setAdapter(adapter)
        autoTextView.threshold = 1



        searchRV= findViewById(R.id.searchRecyclerView)
        searchRV.adapter = searchRVAdapter
        searchRV.setHasFixedSize(false)
        searchRV.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

        binding.mainAutoTextview.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
                if (p1 == KeyEvent.KEYCODE_ENTER) {
                    val searchServerString = serverUrl.plus(
                        "search?searchString=${binding.mainAutoTextview.text}"
                    )
                    Log.e("searchServerString", searchServerString.toString())
                    GlobalScope.launch {
                        getBoardDataFromServer(serverUrl!!, searchRVAdapter)
                        searchRVAdapter.data = searchList
                        println("제대로 작동해야 하는 serachList : $searchList")
                        searchRVAdapter.notifyDataSetChanged()
                    }
                }
                return false
            }
        })



        searchRVAdapter.data = searchList
        searchRVAdapter.notifyDataSetChanged()
    }

    private suspend fun getBoardDataFromServer(serverUrl: String,searchRVAdapter : QuestionTitleAdapter) = suspendCoroutine<Boolean> {
        Handler(Looper.getMainLooper()).postDelayed({
            val searchServerString = serverUrl.plus(
                "search?searchString=${binding.mainAutoTextview.text}"
            )
            val sendAnswerRecoRequest =
                object : StringRequest(Method.GET, searchServerString,
                    Response.Listener { response ->
                        val jsonArray = JSONArray(response)
                        searchList.clear()
                        idArr.clear()
                        for (i in 0 until jsonArray.length()) {
                            val board = jsonArray.getJSONObject(i)
                            searchList.add(
                                ShowQuestionData(
                                    board.get("bbsTitle").toString(),
                                    board.get("bbsContent").toString(),
                                    board.get("createdAt").toString(),
                                    board.get("boarder").toString(),
                                    board.get("hashTagContent").toString()
                                )
                            )
                            idArr.add(board.get("id").toString().toInt())
                            searchRVAdapter.data = searchList
                            searchRVAdapter.notifyDataSetChanged()
                        }
                        if (jsonArray.length() == 0) {
                            Toast.makeText(
                                applicationContext,
                                "해당 자료가 없습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            val inputMethodManager =
                                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            inputMethodManager.hideSoftInputFromWindow(
                                binding.mainAutoTextview.windowToken,
                                0
                            )
                        }
                    }, Response.ErrorListener { error ->
                        Log.e("ERROR", error.toString())
                    }) {
                }
            val sendAnswerQueue = Volley.newRequestQueue(applicationContext)
            sendAnswerQueue.add(sendAnswerRecoRequest)
        }, 500)

    }


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_down_from_top, R.anim.slide_down_to_bottom)
    }
}