package com.example.qode

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.qode.databinding.ActivityContentWithAnswerBinding
import com.example.qode.recyclerview.AnswerAdapter
import com.example.qode.recyclerview.AnswerData
import com.example.qode.recyclerview.ShowQuestionData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ContentWithAnswerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContentWithAnswerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var thumbsUpClicked = false // Thumbs Up button이 안눌러진 상태면 false, 눌러진 상태면 true다.
        var thumbsDownClicked = false // Thumbs Down button이 안눌러진 상태면 false, 눌러진 상태면 true다.
        val serverUrl =
            getSharedPreferences("sharedPrefs", MODE_PRIVATE).getString("serverUrl", null)
        val nickname = getSharedPreferences("sharedPrefs", MODE_PRIVATE).getString("nickname", null)
        val userid = getSharedPreferences("sharedPrefs", MODE_PRIVATE).getString("userid", null)
        val lastClick = "answer"
        val answerList = intent.getParcelableArrayListExtra<AnswerData>("answerData")


        // lastClick변수는 사용자가 댓글을 어디다 달 지 알기 위해서다.
        // answer라면 답변을 다는 것이고
        // comment1~n이라면 n번째 comment에 댓글을 다는 것이다.

        binding = DataBindingUtil.setContentView(this, R.layout.activity_content_with_answer)

        val answerStr = intent.getStringExtra("content")
        val changeText: Spannable = SpannableString(answerStr)
        //TODO 좀더 수정작업이 필요할 것 같다. 제대로 색이 안변하고 앞에 ```이거부터 변한다. 끝 ```이거는 안변한다.
        if (answerStr != null && answerStr.length >= 3) {
            for (i in 0..answerStr.length - 2) {
                if (i <= answerStr.length - 6 && answerStr[i] == '`' && answerStr[i + 1] == '`' && answerStr[i + 2] == '`') { // 연속으로 세 개(```) 나왔다면, 여는 부분
                    // i가 length-6이하여야 하는 이유는 length-6이여야 j+2까지 해서 answerStr에 접근할 수 있기 때문이다.
                    for (j in i + 3..answerStr.length - 3) {
                        if (answerStr[j] == '`' && answerStr[j + 1] == '`' && answerStr[j + 2] == '`') { // 연속으로 세 개(```) 나왔다면, 닫는 부분
                            for (k in i + 3 until j) {
                                changeText.setSpan(
                                    BackgroundColorSpan(Color.parseColor("#fbe4e4")),
                                    k,
                                    k + 1,
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                ) // i부터 j-1까지 배경을 핑크색으로 바꾼다. (코드 배경)
                            }
                        }

                    }
                }

                if (i <= answerStr.length - 4 && answerStr[i] == '*' && answerStr[i + 1] == '*') { // 연속으로 두 개(**) 나왔다면, 여는 부분
                    for (j in i + 2..answerStr.length - 2) {
                        if (answerStr[j] == '*' && answerStr[j + 1] == '*') { // 연속으로 세 개(**) 나왔다면, 닫는 부분
                            for (k in i + 2 until j) {
                                changeText.setSpan(
                                    StyleSpan(Typeface.BOLD),
                                    k,
                                    k + 1,
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                ) // i부터 j-1까지 글씨체를 진하게(bold) 바꾼다.
                            }
                        }
                    }
                }

            }
        }

        binding.title.text = intent.getStringExtra("title")
        //binding.content.text = intent.getStringExtra("content")
        binding.content.text = changeText
        binding.createdTime.text = intent.getStringExtra("createdTime")
        binding.recoCnt.text = intent.getStringExtra("recoCnt")

        val answerDataList = mutableListOf<AnswerData>()
        val posNum = IntArray(999)

        if (answerList != null) {
            binding.noAnswerTV.visibility = View.GONE
            for (i in 0 until answerList.size) {
                answerDataList.add(
                    AnswerData(
                        answerList[i].answer,
                        answerList[i].writer,
                        answerList[i].comment,
                        answerList[i].recoCnt,
                        answerList[i].answerId
                    )
                )
                posNum[i] = answerList[i].answerId.toInt()
            }
        } else { // 답변이 하나도 달리지 않았을 때 UI가 비어있는 것이 좋지 않으므로 채워준다.
            binding.noAnswerTV.visibility = View.VISIBLE
        }

        val answerRecyclerView = findViewById<RecyclerView>(R.id.answerRecyclerView)
        val adapter = AnswerAdapter(applicationContext, posNum)
        answerRecyclerView.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)


        // 아래는 answerDatalist에 오류가 있을 때 확인하기 위한 코드
//        answerDataList.add(
//            AnswerData(
//                "가장 큰 차이라면 cout은 출력하려는 값의 형(type)을 컴파일러가 검사할 수 있는 반면에 printf()는 그렇지 못하다는 것입니다. \n",
//                "홍길동",
//                "1",
//                "2"
//            )
//        )
//        answerDataList.add(AnswerData("Kotlin은 세계 최고의 상용 개발 도", "강태공", "3", " -2"))
//        answerDataList.add(
//            AnswerData(
//                "2018년 카카오가 카카오톡 메시징 서버에 Kotlin을 사용해 본 결과, 코드량이 비약적으로 감소하고 생산성이 대폭 향상되었다고 한다.",
//                "오카카",
//                "0",
//                "4"
//            )
//        )

        answerRecyclerView.adapter = adapter
        answerRecyclerView.setHasFixedSize(false) // ?
        adapter.data = answerDataList
        adapter.notifyDataSetChanged()


        adapter.setOnItemClickListener(object : AnswerAdapter.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                binding.commentET.text.clear()
                binding.commentET.hint = "댓글을 입력해주세요."
            }

            override suspend fun onRecoAddButtonClick(
                v: View?,
                position: Int,
                answerIdArray: IntArray
            ) =
                suspendCoroutine<Boolean> { //TODO 이 부분부터 하자 210225
                    var temp = false
                    println("##### + ${answerIdArray[position]}")
                    //temp = updateAnswerAddReco(serverUrl!!, userid!!, position, answerIdArray)
                    Handler(Looper.getMainLooper()).postDelayed({
                        var sendAnswerRecoServerString = serverUrl.plus(
                            "likes/answer/${userid}/${
                                answerIdArray[position]
                            }"
                        )
                        val sendAnswerRecoRequest =
                            object : StringRequest(Method.POST, sendAnswerRecoServerString,
                                Response.Listener { response ->
                                    val jsonObject = JSONObject(response)
                                    Log.e("sendAnswerRequestState", jsonObject.getString("state"))
                                    if (jsonObject.getString("state") == "alreadyLiked") {
                                        Toast.makeText(
                                            applicationContext,
                                            "이미 누르셨습니다.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        it.resume(true)
                                    }else if(jsonObject.getString("state") == "LikedSuccess"){
                                        it.resume(false)
                                    }
                                }, Response.ErrorListener { error ->
                                    Log.e("ERROR", error.toString())
                                }) {
                                override fun getParams(): MutableMap<String, String> {
                                    val params: MutableMap<String, String> = HashMap()
                                    params["recommend"] = 1.toString()
                                    return params
                                }
                            }
                        val sendAnswerQueue = Volley.newRequestQueue(applicationContext)
                        sendAnswerQueue.add(sendAnswerRecoRequest)
                    }, 500)
                }


            override suspend fun onRecoSubtractButtonClick(
                v: View?,
                position: Int,
                answerIdArray: IntArray
            ) =
                suspendCoroutine<Boolean> { //TODO 이 부분부터 하자 210225
                    var temp = false
                    println("##### + ${answerIdArray[position]}")
                    //temp = updateAnswerAddReco(serverUrl!!, userid!!, position, answerIdArray)
                    Handler(Looper.getMainLooper()).postDelayed({
                        var sendAnswerRecoServerString = serverUrl.plus(
                            "likes/answer/${userid}/${
                                answerIdArray[position]
                            }"
                        )
                        val sendAnswerRecoRequest =
                            object : StringRequest(Method.POST, sendAnswerRecoServerString,
                                Response.Listener { response ->
                                    val jsonObject = JSONObject(response)
                                    Log.e("sendAnswerRequestState", jsonObject.getString("state"))
                                    if (jsonObject.getString("state") == "alreadyLiked") {
                                        Toast.makeText(
                                            applicationContext,
                                            "이미 누르셨습니다.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        it.resume(true)
                                    }else if(jsonObject.getString("state") == "LikedSuccess"){
                                        it.resume(false)
                                    }
                                }, Response.ErrorListener { error ->
                                    Log.e("ERROR", error.toString())
                                }) {
                                override fun getParams(): MutableMap<String, String> {
                                    val params: MutableMap<String, String> = HashMap()
                                    params["recommend"] = (-1).toString()
                                    return params
                                }
                            }
                        val sendAnswerQueue = Volley.newRequestQueue(applicationContext)
                        sendAnswerQueue.add(sendAnswerRecoRequest)
                    }, 500)
                }

        })

        binding.writeAnswerPencilImageButton.setOnClickListener {
            binding.commentET.text.clear()
            binding.commentET.hint = "답변을 입력해주세요."
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

        }

        binding.thumbsUp.setOnClickListener {
            thumbsUpClicked = if (!thumbsUpClicked) { // 클릭되지 않은 상황이라면
                var alreadyChanged = false
                GlobalScope.launch {
                    alreadyChanged = updateReco(serverUrl!!, userid!!, 1)
                    if (!alreadyChanged) {
                        binding.thumbsUp.setImageResource(R.drawable.ic_baseline_thumb_up_24_yellow) // Thumbs UP을 노란색으로 바꾸고
                    }
                }
                true // 클릭되었으니 thumbsUpClicked를 true값으로 처리한다.
            } else { // 클릭 된 상황이라면
                binding.thumbsUp.setImageResource(R.drawable.ic_baseline_thumb_up_24_gray) // 회색으로 바꿔 클릭 처리를 해제하고
                false // 클릭된 상황이 아니므로 false 처리한다.
            }
        }
        binding.thumbsDown.setOnClickListener {
            thumbsDownClicked = if (!thumbsDownClicked) { // 클릭되지 않은 상황이라면
                var alreadyChanged = false
                GlobalScope.launch {
                    alreadyChanged = updateReco(serverUrl!!, userid!!, -1)
                    if (!alreadyChanged) {
                        binding.thumbsDown.setImageResource(R.drawable.ic_baseline_thumb_down_24_yellow) // Thumbs UP을 노란색으로 바꾸고
                    }
                }
                true // 클릭되었으니 thumbsUpClicked를 true값으로 처리한다.
            } else { // 클릭 된 상황이라면
                binding.thumbsDown.setImageResource(R.drawable.ic_baseline_thumb_down_24_gray) // 회색으로 바꿔 클릭 처리를 해제하고
                false // 클릭된 상황이 아니므로 false 처리한다.
            }
        }

        binding.sendAnswerImageButton.setOnClickListener {
            var sendAnswerServerString = serverUrl!!.plus(
                "answers/${userid}/${
                    intent.getStringExtra("boardNum").toString()
                }"
            )
            if (lastClick == "answer") {
                val sendAnswerRequest = object : StringRequest(Method.POST, sendAnswerServerString,
                    Response.Listener { response ->
                        val jsonObject = JSONObject(response)
                        Log.e("sendAnswerRequestState", jsonObject.getString("state"))
                        if (jsonObject.getString("state").toString() == "answerSuccess") {
                            binding.commentET.text.clear()
                            val inputMethodManager =
                                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            inputMethodManager.hideSoftInputFromWindow(
                                binding.commentET.windowToken,
                                0
                            )
                        }
                    }, Response.ErrorListener { error ->
                        Log.e("ERROR", error.toString())
                    }) {
                    override fun getParams(): MutableMap<String, String> {
                        val params: MutableMap<String, String> = HashMap()
                        params["answerContent"] = binding.commentET.text.toString()
                        return params
                    }
                }
                val sendAnswerQueue = Volley.newRequestQueue(applicationContext)
                sendAnswerQueue.add(sendAnswerRequest)
            }
        }
    }

    private suspend fun updateAnswerAddReco(
        serverUrl: String,
        userid: String,
        pos: Int,
        answerIdArray: IntArray
    ) = suspendCoroutine<Boolean> {
        Handler(Looper.getMainLooper()).postDelayed({
            var sendAnswerRecoServerString = serverUrl.plus(
                "likes/answer/${userid}/${
                    answerIdArray[pos]
                }"
            )
            val sendAnswerRecoRequest =
                object : StringRequest(Method.POST, sendAnswerRecoServerString,
                    Response.Listener { response ->
                        val jsonObject = JSONObject(response)
                        Log.e("sendAnswerRequestState", jsonObject.getString("state"))
                        if (jsonObject.getString("state") == "alreadyLiked") {
                            Toast.makeText(applicationContext, "이미 누르셨습니다.", Toast.LENGTH_SHORT)
                                .show()
                            it.resume(true)
                        }
                    }, Response.ErrorListener { error ->
                        Log.e("ERROR", error.toString())
                    }) {
                    override fun getParams(): MutableMap<String, String> {
                        val params: MutableMap<String, String> = HashMap()
                        params["recommend"] = 1.toString()
                        return params
                    }
                }
            val sendAnswerQueue = Volley.newRequestQueue(applicationContext)
            sendAnswerQueue.add(sendAnswerRecoRequest)
        }, 500)
    }

    private suspend fun updateReco(serverUrl: String, userid: String, plusOrMinus: Int) =
        suspendCoroutine<Boolean> {
            Handler(Looper.getMainLooper()).postDelayed({
                val sendRecoAddServerString = serverUrl.plus(
                    "likes/board/${userid}/${
                        intent.getStringExtra("boardNum").toString()
                    }"
                ) // board ID를 추가해야 한다.

                var alreadyChanged = false

                Log.e("recoADdUrl", sendRecoAddServerString)
                val sendRecoAddRequest =
                    object : StringRequest(Method.POST, sendRecoAddServerString,
                        Response.Listener { response ->
                            val jsonObject = JSONObject(response)
                            Log.e("sendAddState", jsonObject.getString("state"))
                            if (jsonObject.getString("state") == "alreadyLiked") {
                                Toast.makeText(this, "이미 누르셨습니다.", Toast.LENGTH_SHORT).show()
                                alreadyChanged = true
                            }
                            it.resume(alreadyChanged)
                        }, Response.ErrorListener { error ->
                            Log.e("ERROR", error.toString())
                        }) {
                        override fun getParams(): MutableMap<String, String> {
                            val params: MutableMap<String, String> = HashMap()
                            if (plusOrMinus == 1) {
                                params["recommend"] = 1.toString()
                            } else if (plusOrMinus == -1) {
                                params["recommend"] = (-1).toString()
                            }
                            return params
                        }
                    }
                val queue = Volley.newRequestQueue(applicationContext)
                queue.add(sendRecoAddRequest)
            }, 500)
        }


}