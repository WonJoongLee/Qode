package com.example.qode

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.Spannable
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.MultiAutoCompleteTextView.CommaTokenizer
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.qode.databinding.ActivityMainBinding
import com.example.qode.recyclerview.QuestionTitleAdapter
import com.example.qode.recyclerview.ShowQuestionData
import com.example.qode.register.LoginActivity
import com.example.qode.register.RegisterActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.hypot


//TODO 회원가입 시 글자 제한 두기
//TODO 게시판 댓글 GET, POST

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val boardData = mutableListOf<ShowQuestionData>()
    private var isFabOpen = false
    var bbsSize: Int = 0
    var commaCnt = 0 // 글쓰기 화면에서 해시태그가 몇 개인지 확인하기 위해 콤마를 센다. 해시태그는 총 세개까지 되므로 콤마가 총 세 개 이상이면 안된다
    private var idArr = mutableListOf<Int>()

    @SuppressLint("ResourceAsColor", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        var bottomSheetDialog: BottomSheetDialog

        //TODO 날짜 부분
        val now = System.currentTimeMillis()
        val mDate = Date(now)
        val simpleDate : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val getTime = simpleDate.format(mDate)
        println("@@@@@@ $getTime")


        var adapter = QuestionTitleAdapter(applicationContext, idArr)
        val recyclerView = findViewById<RecyclerView>(R.id.mainRecyclerView)


        val buttonClicked =
            mutableMapOf<String, Boolean>() // scroll view의 버튼이 클릭 됐는지 확인하기 위한 mutableMap
        // 초기 설정값은 모두 false고, 클릭 된 상태를 true로 본다

        val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        Log.e("Nickname", sharedPreferences.getString("nickname", "").toString())

        editor.apply {
            putString("serverUrl", "http://017595faff79.ngrok.io/") // server Url 추가
        }.apply()

        val serverString = sharedPreferences.getString("serverUrl", null)
        val boardServerString = serverString.plus("boards")
        val postBoardServerString = serverString.plus("boards/test12")
        println("@@@@ $postBoardServerString")
        getBoardData(boardServerString)

        Log.e("ServerString", serverString!!)

        buttonClicked["C"] = false
        buttonClicked["CPP"] = false
        buttonClicked["JAVA"] = false
        buttonClicked["ALGORITHM"] = false
        buttonClicked["PYTHON"] = false
        buttonClicked["SERVER"] = false
        buttonClicked["WEB"] = false
        buttonClicked["MOBILE"] = false
        buttonClicked["ETC"] = false

        println("@@@ 0번 : $boardData")

        recyclerView.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
//        boardData.add(ShowQuestionData("TextView Title", "여기에는 내용이 들어갑니다.", " ", " "))
//        boardData.add(ShowQuestionData("TextView Title", "여기에는 내용이 들어갑니다.", " ", " "))
//        boardData.add(ShowQuestionData("TextView Title", "여기에는 내용이 들어갑니다.", " ", " "))
//        boardData.add(ShowQuestionData("TextView Title", "여기에는 내용이 들어갑니다.", " ", " "))

        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(false)
//        val request = object : StringRequest(Method.GET, serverString,
//            Response.Listener { response ->
//                val jsonArray =
//                    JSONArray(response) //  JSONObject가 아니라 array로 바로 오므로 JSONArray로 받아야함
//                bbsSize = jsonArray.length()
//                for (i in 0 until jsonArray.length()) {
//                    val j = jsonArray.getJSONObject(i)
//
////                    if(j.get("bbstTitle").toString().isEmpty()){
////                        continue
////                    }
//
//                    boardData.add(
//                        ShowQuestionData(
//                            j.get("bbsTitle").toString(),
//                            j.get("bbsContent").toString(),
//                            j.get("created_at").toString(),
//                            j.get("boarder").toString()
//                        )
//                    )
//                    //boardData.addAll()
//                    //이하는 response 확인용 구문들
//                    //println("title : ${j.get("bbsTitle")}, boarder : ${j.get("boarder")}")
//                    //println("content : ${j.get("bbsContent")}, created_at : ${j.get("created_at")}")
//                    Log.e("Value", j.toString())
//                    //adapter.data = boardData // 데이터를 삽입
//                }
//                adapter.data.addAll(boardData)
//                adapter.notifyDataSetChanged()
//                adapter.data = boardData
//                Log.e("check", "check")
//            }, Response.ErrorListener { error ->
//                Log.e("ERROR", "@@@@ + ${error.toString()}")
//            }) {
//        }
//        val queue = Volley.newRequestQueue(applicationContext)
//        queue.add(request)
        //adapter.data = boardData
        //adapter.data.addAll(boardData) // 데이터를 삽입

        Handler(Looper.getMainLooper()).postDelayed({
            //boardData.add(ShowQuestionData("title", "아아 ```helloworld```입니다. 이거는 **bold**확인해보는 것입니다.", "time", "writer", "#java"))

            adapter.data = boardData // 데이터를 삽입
            adapter.notifyDataSetChanged()
            binding.mainProgressbar.visibility = View.GONE
        }, 1500) // 서버에서 데이터 받아오는 데 시간이 걸리므로 대략 2초 이따가 adapter 연결해서 데이터 보여주는 식으로 함.


        adapter = QuestionTitleAdapter(applicationContext, idArr)
        recyclerView.adapter = adapter


        println("@@@ 2번 : $boardData")

        binding.refreshButton.setOnClickListener {
            binding.refreshButton.animate().rotation(binding.refreshButton.rotation + 720)
                .setDuration(500).start() // 버튼 클릭 시, 0.5초 동안 두 번 회전해서, 새로고침 했다는 것을 알려준다.

            // val getHashtagContentServerString = serverString.plus("search/board?hashtag=${}")// TODO 여기 해시태그 검색 넣기


            //TODO 이 부분 라우터 하나 받아서 검색하는 식으로 다시 하기로 했음
//            var newBoardList = mutableListOf<ShowQuestionData>()
//
//            for (i in boardData) {
//                if (i.hashtag.isNotEmpty()) { // 게시물에 해시태그가 있다면
//                    val hashTagList = i.hashtag.split("#")
//                    for(j in 1 until hashTagList.size){
//                        Log.e("확인", hashTagList[j])
//                        if (buttonClicked[hashTagList[j]]!!) { // 사용자가 선택한 해시태그와, 게시물의 해시태그가 하나라도 일치하면 추가
//                            newBoardList.add(i)
//                            break // 중복으로 더 들어갈 수 있으므로 바로 탈출
//                        }
//                    }
//                }
//            }
//            adapter.data.clear()
//            adapter.data = newBoardList
//            adapter.notifyDataSetChanged()

        }

        binding.cLangButton.setOnClickListener {
            if (buttonClicked["C"] == false) { // 처음에는 모두 false 처리 되어 있는 상태이므로, 조건문도 false부터 검사한다.
                binding.cLangButton.setBackgroundResource(R.drawable.button_language_background_on)
                binding.cLangButton.setTextColor(Color.parseColor("#FFFFFF"))
                buttonClicked["C"] = true
            } else {
                binding.cLangButton.setBackgroundResource(R.drawable.button_language_background)
                binding.cLangButton.setTextColor(Color.parseColor("#000000"))
                buttonClicked["C"] = false
            }
        }

        binding.cppLangButton.setOnClickListener {
            if (buttonClicked["CPP"] == false) { // 처음에는 모두 false 처리 되어 있는 상태이므로, 조건문도 false부터 검사한다.
                binding.cppLangButton.setBackgroundResource(R.drawable.button_language_background_on)
                binding.cppLangButton.setTextColor(Color.parseColor("#FFFFFF"))
                buttonClicked["CPP"] = true
            } else {
                binding.cppLangButton.setBackgroundResource(R.drawable.button_language_background)
                binding.cppLangButton.setTextColor(Color.parseColor("#000000"))
                buttonClicked["CPP"] = false
            }
        }

        binding.javaLangButton.setOnClickListener {
            if (buttonClicked["JAVA"] == false) { // 처음에는 모두 false 처리 되어 있는 상태이므로, 조건문도 false부터 검사한다.
                binding.javaLangButton.setBackgroundResource(R.drawable.button_language_background_on)
                binding.javaLangButton.setTextColor(Color.parseColor("#FFFFFF"))
                buttonClicked["JAVA"] = true
            } else {
                binding.javaLangButton.setBackgroundResource(R.drawable.button_language_background)
                binding.javaLangButton.setTextColor(Color.parseColor("#000000"))
                buttonClicked["JAVA"] = false
            }
        }

        binding.algorithmButton.setOnClickListener {
            if (buttonClicked["ALGORITHM"] == false) { // 처음에는 모두 false 처리 되어 있는 상태이므로, 조건문도 false부터 검사한다.
                binding.algorithmButton.setBackgroundResource(R.drawable.button_language_background_on)
                binding.algorithmButton.setTextColor(Color.parseColor("#FFFFFF"))
                buttonClicked["ALGORITHM"] = true
            } else {
                binding.algorithmButton.setBackgroundResource(R.drawable.button_language_background)
                binding.algorithmButton.setTextColor(Color.parseColor("#000000"))
                buttonClicked["ALGORITHM"] = false
            }
        }

        binding.pythonLangButton.setOnClickListener {
            if (buttonClicked["PYTHON"] == false) { // 처음에는 모두 false 처리 되어 있는 상태이므로, 조건문도 false부터 검사한다.
                binding.pythonLangButton.setBackgroundResource(R.drawable.button_language_background_on)
                binding.pythonLangButton.setTextColor(Color.parseColor("#FFFFFF"))
                buttonClicked["PYTHON"] = true
            } else {
                binding.pythonLangButton.setBackgroundResource(R.drawable.button_language_background)
                binding.pythonLangButton.setTextColor(Color.parseColor("#000000"))
                buttonClicked["PYTHON"] = false
            }
        }

        binding.serverButton.setOnClickListener {
            if (buttonClicked["SERVER"] == false) { // 처음에는 모두 false 처리 되어 있는 상태이므로, 조건문도 false부터 검사한다.
                binding.serverButton.setBackgroundResource(R.drawable.button_language_background_on)
                binding.serverButton.setTextColor(Color.parseColor("#FFFFFF"))
                buttonClicked["SERVER"] = true
            } else {
                binding.serverButton.setBackgroundResource(R.drawable.button_language_background)
                binding.serverButton.setTextColor(Color.parseColor("#000000"))
                buttonClicked["SERVER"] = false
            }
        }

        binding.webButton.setOnClickListener {
            if (buttonClicked["WEB"] == false) { // 처음에는 모두 false 처리 되어 있는 상태이므로, 조건문도 false부터 검사한다.
                binding.webButton.setBackgroundResource(R.drawable.button_language_background_on)
                binding.webButton.setTextColor(Color.parseColor("#FFFFFF"))
                buttonClicked["WEB"] = true
            } else {
                binding.webButton.setBackgroundResource(R.drawable.button_language_background)
                binding.webButton.setTextColor(Color.parseColor("#000000"))
                buttonClicked["WEB"] = false
            }
        }

        binding.mobileButton.setOnClickListener {
            if (buttonClicked["MOBILE"] == false) { // 처음에는 모두 false 처리 되어 있는 상태이므로, 조건문도 false부터 검사한다.
                binding.mobileButton.setBackgroundResource(R.drawable.button_language_background_on)
                binding.mobileButton.setTextColor(Color.parseColor("#FFFFFF"))
                buttonClicked["MOBILE"] = true
            } else {
                binding.mobileButton.setBackgroundResource(R.drawable.button_language_background)
                binding.mobileButton.setTextColor(Color.parseColor("#000000"))
                buttonClicked["MOBILE"] = false
            }
        }

        binding.iotButton.setOnClickListener {
            if (buttonClicked["IOT"] == false) { // 처음에는 모두 false 처리 되어 있는 상태이므로, 조건문도 false부터 검사한다.
                binding.iotButton.setBackgroundResource(R.drawable.button_language_background_on)
                binding.iotButton.setTextColor(Color.parseColor("#FFFFFF"))
                buttonClicked["IOT"] = true
            } else {
                binding.iotButton.setBackgroundResource(R.drawable.button_language_background)
                binding.iotButton.setTextColor(Color.parseColor("#000000"))
                buttonClicked["IOT"] = false
            }
        }

        binding.etcButton.setOnClickListener {
            if (buttonClicked["ETC"] == false) { // 처음에는 모두 false 처리 되어 있는 상태이므로, 조건문도 false부터 검사한다.
                binding.etcButton.setBackgroundResource(R.drawable.button_language_background_on)
                binding.etcButton.setTextColor(Color.parseColor("#FFFFFF"))
                buttonClicked["ETC"] = true
            } else {
                binding.etcButton.setBackgroundResource(R.drawable.button_language_background)
                binding.etcButton.setTextColor(Color.parseColor("#000000"))
                buttonClicked["ETC"] = false
            }
        }


        val mSwipeRefreshLayout: SwipeRefreshLayout = findViewById(R.id.mainSwipeLayout)
        mSwipeRefreshLayout.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                Handler(Looper.getMainLooper()).postDelayed({
                    mSwipeRefreshLayout.isRefreshing = false
                }, 1500) // 1.5초 후 새로고침 멈추는 코드
            }
        })

        binding.fab.setOnClickListener {
            if (sharedPreferences.getBoolean("isLogin", false)) { // TODO 원래 false여야 한다.
                if (!isFabOpen) { // 게시글 쓰는 창이 지금 화면에 나오고 있는 상태면

                    binding.postContent.hint =
                        " ■ 코드를 입력하시려면 아래와 같이 코드 앞과 뒤에 ```를 표시해주세요.\n ```\n printf(\"hello world\");\n ```\n\n ■ 글씨를 진하게 하시려면" +
                                "아래와 같이 문자 앞과 뒤에 **를 표시해주세요.\n **대한민국**"

                    //추천 해시태그 목록
                    val multiAutoTextStrings = arrayOf(
                        "#c",
                        "#c++",
                        "#java",
                        "#algorithm",
                        "#python",
                        "#server",
                        "#web",
                        "#mobile",
                        "#iot",
                        "#etc"
                    )
                    // edittext자동완성시켜주는 부분
                    val multiAutoTextAdapter = ArrayAdapter(
                        this,
                        android.R.layout.simple_list_item_1,
                        multiAutoTextStrings
                    )
                    val multiAutoTextView =
                        findViewById<MultiAutoCompleteTextView>(R.id.autoCompleteTag)

                    multiAutoTextView.setTokenizer(CommaTokenizer())

                    multiAutoTextView.setAdapter(multiAutoTextAdapter)
                    multiAutoTextView.threshold = 1 // 한 글자부터 출력될 수 있도록 설정

                    /**
                     * 글쓰기 태그 입력시 자동으로 hashtag 추천해주는 것 중 string을 다루는 부분
                     * */
                    multiAutoTextView.onItemClickListener =
                        AdapterView.OnItemClickListener { parent, view, position, id ->
                            var commaCnt = 0
                            val tempText = multiAutoTextView.text
                            val changeText: Spannable = tempText
                            val autoTextinChar = multiAutoTextView.text
                            for (i in 0 until multiAutoTextView.length()) {
                                if (autoTextinChar[i] != ' ' && autoTextinChar[i] != ',') { // 색을 넣어주는 부분
                                    changeText.setSpan(
                                        BackgroundColorSpan(Color.parseColor("#fbe4e4")),
                                        i,
                                        i + 1,
                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                    ) // 1~4는 인덱스. 한글도 한 자로 친다.
                                }
                                if (autoTextinChar[i] == ',') commaCnt++
                            }
                            if (commaCnt == 3) {
                                multiAutoTextView.setText(multiAutoTextView.text.dropLast(2))
                                multiAutoTextView.setSelection(multiAutoTextView.length())
                            }
                        }

                    binding.sendButton.setOnClickListener {
                        val hashtagStr = multiAutoTextView.text
                        var commaCnt = 0
                        for (i in hashtagStr) {
                            if (i == ',') {
                                commaCnt++
                            }
                        }

                        var sendBoardState = ""

                        /**
                         * 해시태그 중복 검사하는 부분입니다.
                         * 해시태그가 중복되면 서버로 보내지 않습니다.
                         * */
                        var hashTagDuplicates = false // 해시태그 중복이 있는지 확인한다. 중복이 있으면 true로 바꾼다.
                        val hashTags = binding.autoCompleteTag.text.split(", ")
                        for(hashTag in hashTags){
                            if(hashTags.count{it==hashTag} >= 2){
                                Toast.makeText(applicationContext, "해시태그가 중복됩니다.", Toast.LENGTH_SHORT).show()
                                hashTagDuplicates = true // 중복이 있으므로 true로 바꾼다.
                            }
                        }

                        if (commaCnt <= 2 && !hashTagDuplicates) {
                            val requestSendBoard =
                                object : StringRequest(Method.POST, postBoardServerString,
                                    Response.Listener { response ->
                                        val jsonObject =
                                            JSONObject(response) //  JSONObject가 아니라 array로 바로 오므로 JSONArray로 받아야함
                                        sendBoardState = jsonObject.getString("state")
                                        Log.e("sendBoardState", sendBoardState)
                                        if(sendBoardState == "boardSuccess"){
                                            binding.postContent.text.clear()
                                            binding.postTitle.text.clear()
                                            binding.autoCompleteTag.text.clear() // boardSuccess가 나와서 DB 전송이 성공적으로 완료되면 edittext의 값들을 모두 비워주고
                                            showReveal() // 창을 닫는다.
                                        }
                                    }, Response.ErrorListener { error ->
                                        Log.e("ERROR", error.toString())
                                    }) {
                                    override fun getParams(): Map<String, String> {
                                        val params: MutableMap<String, String> = HashMap()
                                        params["bbsTitle"] = binding.postTitle.text.toString()
                                        params["bbsContent"] = binding.postContent.text.toString()
                                        params["hashTagContent"] = binding.autoCompleteTag.text.toString().replace(", ","")
                                        Log.e("hashtag", binding.autoCompleteTag.text.toString().replace(", ",""))
                                        return params
                                    }
                                }
                            requestSendBoard.retryPolicy = object : RetryPolicy {
                                override fun retry(error: VolleyError?) {

                                }

                                override fun getCurrentRetryCount(): Int {
                                    return 50000
                                }

                                override fun getCurrentTimeout(): Int {
                                    return 50000
                                }

                            }
                            val postQueue = Volley.newRequestQueue(this)
                            postQueue.add(requestSendBoard)
                        } else if (commaCnt > 2) {
                            Toast.makeText(this, "해시태그는 최대 세개입니다.", Toast.LENGTH_SHORT).show()
                        }

//                        val request = object : StringRequest(Method.POST, boardServerString,
//                            Response.Listener { response ->
//                                val jsonArray =
//                                    JSONArray(response) //  JSONObject가 아니라 array로 바로 오므로 JSONArray로 받아야함
//                                bbsSize = jsonArray.length()
//                            }, Response.ErrorListener { error ->
//                                Log.e("ERROR", error.toString())
//                            }) {
//                            override fun getParams(): Map<String, String> {
//                                val params: MutableMap<String, String> = HashMap()
//                                params["bbsTitle"] = binding.postTitle.text.toString()
//                                params["bbsContent"] = binding.postContent.text.toString()
//
//                                return params
//                            }
//                        }
//                        val postQueue = Volley.newRequestQueue(this)
//                        postQueue.add(request)

                        if(sendBoardState == "boardSuccess"){
                                // boardSuccess가 나오면 게시글 전송이 잘 되었다는 것이므로
                                // 다시 창을 닫아줍니다.
                            showReveal()
                            println("들어옴.")
                        }

                    }
                }
                showReveal()
            } else {
                Toast.makeText(this, "먼저 로그인 해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        val sheetView: View = LayoutInflater.from(applicationContext)
            .inflate(R.layout.bottom_sheet_layout, findViewById(R.id.bottom_sheet), false)
        Log.d("Login Data", "${sharedPreferences.getBoolean("isLogin", false)}") //
        setBottomSheetVisibility(sharedPreferences, sheetView)

        binding.hamburgerImageButton.setOnClickListener {
            bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetTheme)
            sheetView.findViewById<LinearLayout>(R.id.registerBottomSheetButton)
                .setOnClickListener {
                    val intent = Intent(this, RegisterActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(
                        R.anim.slide_x_minus_hundred_to_zero,
                        R.anim.slide_x_zero_to_hundred
                    )
                    bottomSheetDialog.dismiss()
                }
            sheetView.findViewById<LinearLayout>(R.id.loginBottomSheetButton).setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                overridePendingTransition(
                    R.anim.slide_x_minus_hundred_to_zero,
                    R.anim.slide_x_zero_to_hundred
                )
                bottomSheetDialog.dismiss()
            }
            sheetView.findViewById<LinearLayout>(R.id.searchBottomSheetButton).setOnClickListener {
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down)
                bottomSheetDialog.dismiss()
            }
            sheetView.findViewById<LinearLayout>(R.id.myProfileButton).setOnClickListener {
                val intent = Intent(this, MyProfileActivity::class.java)
                startActivity(intent)
                overridePendingTransition(
                    R.anim.slide_x_minus_hundred_to_zero,
                    R.anim.slide_x_zero_to_hundred
                )
                bottomSheetDialog.dismiss()
            }
            sheetView.findViewById<LinearLayout>(R.id.logoutButton)
                .setOnClickListener {//TODO 로그아웃 서버와 연결
                    editor.apply {
                        putBoolean("isLogin", false)//로그아웃했으므로 isLogin Boolean으로 처리
                    }.apply()
                    Log.d("Login Data", "${sharedPreferences.getBoolean("isLogin", false)}")
                    setBottomSheetVisibility(
                        sharedPreferences,
                        sheetView
                    ) // 로그아웃을 하면 Bottom Sheet View에서 보여지는 값들 변경하기
                    bottomSheetDialog.dismiss()
                }
            if (sheetView.parent != null) {
                (sheetView.parent as ViewGroup).removeAllViews()
            }
            bottomSheetDialog.setContentView(sheetView)
            bottomSheetDialog.show()
        }

    }

//    fun View.hideKeyboard() {
//        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(windowToken, 0)
//    }
//
//    fun View.showKeyboard() {
//        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
//    }

    //TODO private suspend fun serverLogout() = suspendCoroutine<> {  }

    private fun setBottomSheetVisibility(sharedPreferences: SharedPreferences, sheetView: View) {
        if (!sharedPreferences.getBoolean("isLogin", false)) { // 만약 로그인이 되어 있지 않은 상황이라면,
            // Default는 false다. 왜냐하면 처음에는 로그인 된 상황이 아니므로
            sheetView.findViewById<LinearLayout>(R.id.logoutButton).visibility = View.GONE
            sheetView.findViewById<LinearLayout>(R.id.myProfileBottomLinearLayout).visibility =
                View.GONE
            sheetView.findViewById<LinearLayout>(R.id.myProfileButton).visibility = View.GONE
            sheetView.findViewById<LinearLayout>(R.id.searchBottomLinearLayout).visibility =
                View.GONE

            sheetView.findViewById<LinearLayout>(R.id.loginBottomSheetButton).visibility =
                View.VISIBLE
            sheetView.findViewById<LinearLayout>(R.id.loginBottomLinearLayout).visibility =
                View.VISIBLE
            sheetView.findViewById<LinearLayout>(R.id.registerBottomSheetButton).visibility =
                View.VISIBLE
            sheetView.findViewById<LinearLayout>(R.id.registerBottomLinearLayout).visibility =
                View.VISIBLE
        } else { // 로그인이 되어 있는 상황이라면,
            sheetView.findViewById<LinearLayout>(R.id.logoutButton).visibility = View.VISIBLE
            sheetView.findViewById<LinearLayout>(R.id.myProfileBottomLinearLayout).visibility =
                View.VISIBLE
            sheetView.findViewById<LinearLayout>(R.id.myProfileButton).visibility = View.VISIBLE
            sheetView.findViewById<LinearLayout>(R.id.searchBottomLinearLayout).visibility =
                View.VISIBLE

            sheetView.findViewById<LinearLayout>(R.id.loginBottomSheetButton).visibility = View.GONE
            sheetView.findViewById<LinearLayout>(R.id.loginBottomLinearLayout).visibility =
                View.GONE
            sheetView.findViewById<LinearLayout>(R.id.registerBottomSheetButton).visibility =
                View.GONE
            sheetView.findViewById<LinearLayout>(R.id.registerBottomLinearLayout).visibility =
                View.GONE
        }
    }


    private fun showReveal() {
        val centerX = binding.fab.x + binding.fab.width / 2 // 애니메이션 중심 x좌표 지정
        val centerY = binding.fab.y + binding.fab.height / 2 // 애니메이션 중심 y좌표 지정
        //target view 지정
        val radius =
            hypot(binding.layoutContent.width.toDouble(), binding.layoutContent.height.toDouble())

        if (isFabOpen) {
            Log.e("FAB", "Fab Status : $isFabOpen")

            binding.fab.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#009688"))
            binding.fab.setImageResource(R.drawable.pencil)
            binding.bottomAppBar.performShow()
            var revealAnimator: Animator = ViewAnimationUtils.createCircularReveal(
                binding.layoutContent,
                centerX.toInt(), centerY.toInt(), radius.toFloat(), 0F
            )//start radius : 애니메이션 시작 지점 원의 반경 지정, end radius : 애니메이션 종료 지점 원의 반경 지정
            revealAnimator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator?) {

                }

                override fun onAnimationEnd(p0: Animator?) {
                    binding.layoutContent.visibility = View.INVISIBLE
                }

                override fun onAnimationCancel(p0: Animator?) {
                    TODO("Not yet implemented")
                }

                override fun onAnimationRepeat(p0: Animator?) {
                    TODO("Not yet implemented")
                }

            })//TODO
            revealAnimator.duration = 300
            revealAnimator.start()
        } else {
            Log.e("FAB", "Fab Status : $isFabOpen")

            binding.bottomAppBar.performHide()
            binding.fab.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.fab.setImageResource(R.drawable.ic_baseline_arrow_back_24)
            var revealAnimator: Animator = ViewAnimationUtils.createCircularReveal(
                binding.layoutContent,
                centerX.toInt(),
                centerY.toInt(),
                0F,
                radius.toFloat()
            )
            revealAnimator.duration = 300
            binding.layoutContent.visibility = View.VISIBLE
            revealAnimator.start()
        }

        isFabOpen = !isFabOpen
    }

    private fun getBoardData(serverString: String) {
        GlobalScope.launch {
            getBoardDataFromServer(serverString)
        }
    }

    private suspend fun getBoardDataFromServer(bsString: String) = suspendCoroutine<Boolean> {
        Handler(Looper.getMainLooper()).postDelayed({
            val request = object : StringRequest(Method.GET, bsString,
                Response.Listener { response ->
                    val jsonArray =
                        JSONArray(response) //  JSONObject가 아니라 array로 바로 오므로 JSONArray로 받아야함
                    bbsSize = jsonArray.length()
                    for (i in 0 until jsonArray.length()) {
                        val j = jsonArray.getJSONObject(i)
                        boardData.add(
                            ShowQuestionData(
                                j.get("bbsTitle").toString(),
                                j.get("bbsContent").toString(),
                                j.get("createdAt").toString(),
                                j.get("boarder").toString(),
                                j.get("hashTagContent").toString()
                            )
                        )
                        idArr.add(j.get("id").toString().toInt())
                    }
                    it.resume(true)
                }, Response.ErrorListener { error ->
                    Log.e("ERROR", error.toString())
                }) {
            }
            val queue = Volley.newRequestQueue(this)
            queue.add(request)
        }, 500)

    }

    override fun onBackPressed() {
        if (!isFabOpen) {
            super.onBackPressed()
        }
    }

}


