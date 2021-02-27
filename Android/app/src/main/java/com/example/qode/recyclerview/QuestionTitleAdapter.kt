package com.example.qode.recyclerview

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Looper
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.qode.ContentActivity
import com.example.qode.R
import com.example.qode.ContentWithAnswerActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class QuestionTitleAdapter(private val context: Context, private val idArr: MutableList<Int>) :

    RecyclerView.Adapter<QuestionTitleViewHolder>() {

    var data = mutableListOf<ShowQuestionData>()

    private val answerDataList = ArrayList<AnswerData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionTitleViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_questions, parent, false)

        return QuestionTitleViewHolder(view)
    }

    override fun getItemCount(): Int = data.size
    override fun onBindViewHolder(holder: QuestionTitleViewHolder, position: Int) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("sharedPrefs", MODE_PRIVATE)
        val serverString = sharedPreferences.getString("serverUrl", null)

        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            val intent = Intent(
                holder.itemView.context,
                //ContentActivity::class.java // TODO 원래 여기로 이동함.
                ContentWithAnswerActivity::class.java
            ) // 여기서 contentActivity로 넘겨줄 때 title, 댓글 등 정보들을 서버로부터 받아와서 같이 넘겨주면 될 것 같다.
            //println("!!!! 중요 : ${idArr[position]}")
            //val posBoardUrl = serverString.plus("boards/test12/").plus((position + 1).toString())
            val posBoardUrl = serverString.plus("boards/test12/").plus((idArr[position]).toString())
            /** 서버 연결할 때 이 부분 주석 해제 해주면 된다.
             *
             */
            GlobalScope.launch {
                val data = getDataFromServer(posBoardUrl)
                println("@@@@@ ${data["title"]}") // 값을 Map으로 받는다. key에는 title, value에는 title string값
                Log.e("posBoardURL", posBoardUrl)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("title", data["title"])
                intent.putExtra("content", data["content"])
                intent.putExtra("viewCnt", data["viewCnt"])
                intent.putExtra("recoCnt", data["reco"])
                intent.putExtra("hashtag", data["hashtag"])
                intent.putExtra("createdTime", data["createdTime"])
                intent.putExtra("writer", data["writer"])
                intent.putExtra(
                    "boardNum",
                    data["boardNum"]
                ) // 첫 번째 parameter는 넘겨줄 이름, 두 번째 parameter는 data에 저장된 key로 접근해서 실제 value가져옴
                intent.putExtra("boardNum", (data["boardNum"]!!.toInt()).toString())
                intent.putExtra("answerId", data["answerId"])
                if (answerDataList.isNotEmpty()) intent.putParcelableArrayListExtra(
                    "answerData",
                    answerDataList
                ) // 답변들 넘겨주는 부분
                startActivity(holder.itemView.context, intent, null) // 새로운 activity로 이동
                answerDataList.clear() // clear를 안해주면 계속 남아 있다.
            }
        }
    }

    private suspend fun getDataFromServer(posBoardUrl: String) =
        suspendCoroutine<MutableMap<String, String>> {
            android.os.Handler(Looper.getMainLooper()).postDelayed({
                val arr = mutableMapOf<String, String>()
                val request = object : StringRequest(Method.GET, posBoardUrl,
                    Response.Listener { response ->
                        val boardJsonObject =
                            JSONObject(response).getJSONObject("board") // 게시물에 대한 값들을 불러오는 부분입니다.
                        val answerJsonArray =
                            JSONObject(response).getJSONArray("answers") // 답변들에 대한 값들을 불러오는 부분입니다.
                        arr["title"] = boardJsonObject.getString("bbsTitle")
                        arr["content"] = boardJsonObject.optString("bbsContent")
                        arr["viewCnt"] = boardJsonObject.optString("bbsViews")
                        arr["reco"] = boardJsonObject.optString("bbsReco")
                        arr["hashtag"] = boardJsonObject.optString("hashTagContent")
                        arr["createdTime"] = boardJsonObject.optString("createdAt")
                        arr["writer"] = boardJsonObject.optString("boarder")
                        arr["boardNum"] = boardJsonObject.optString("id")
                        arr["answerCount"] = boardJsonObject.optString("answerCount")
                        for (i in 0 until answerJsonArray.length()) { // answer가 달린만큼 반복한다.
                            val answerJsonObject = answerJsonArray.getJSONObject(i)
                            answerDataList.add(
                                AnswerData(
                                    answerJsonObject.getString("answerContent"),
                                    answerJsonObject.getString("answerer"),
                                    answerJsonObject.getString("commentCount"),
                                    answerJsonObject.getString("answerReco"),
                                    answerJsonObject.getString("id")
                                )
                            )
                        }
                        println("@@@ title : ${arr["title"]}")
                        it.resume(arr)
                    },
                    Response.ErrorListener { error ->
                        Log.e("error", error.toString())
                    }) {
                }
                val queue = Volley.newRequestQueue(context)
                queue.add(request)
            }, 10)
        }
}

class QuestionTitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
    private val tvContent = itemView.findViewById<TextView>(R.id.tvContent)
    private val tag1 = itemView.findViewById<TextView>(R.id.tag1)
    private val tag2 = itemView.findViewById<TextView>(R.id.tag2)
    private val tag3 = itemView.findViewById<TextView>(R.id.tag3)
    private val uploadDate = itemView.findViewById<TextView>(R.id.uploadDate)

    fun bind(showQuestionData: ShowQuestionData) {
        tvTitle.text = showQuestionData.tvTitle
        uploadDate.text = showQuestionData.tvTime

        val targetStr = showQuestionData.tvContent
        val changeText = SpannableStringBuilder(targetStr)
        val graveArr = mutableListOf<Int>()
        val boldArr = mutableListOf<Int>()
        var graveCnt = 0
        var boldCnt = 0

        if (targetStr != null && targetStr.length >= 3) {
            for (i in 0..targetStr.length - 2) {

                if (i <= targetStr.length - 6 && targetStr[i] == '`' && targetStr[i + 1] == '`' && targetStr[i + 2] == '`' && !graveArr.contains(
                        i
                    )
                ) { // 연속으로 세 개(```) 나왔다면, 여는 부분
                    // i가 length-6이하여야 하는 이유는 length-6이여야 j+2까지 해서 targetStr에 접근할 수 있기 때문이다.
                    for (j in i + 3 until targetStr.length) {
                        if (targetStr[j] == '`' && targetStr[j + 1] == '`' && targetStr[j + 2] == '`') { // 연속으로 세 개(```) 닫는 부분이 나왔다면,
                            graveCnt += 2
                            graveArr.add(i)
                            graveArr.add(j) // 뒤에 배경이 들어가게 처리된 부분에서 `를 지우기 위해 `인 부분을 한 쌍씩 넣는다.
                            println("$$$ i : $i, j : $j")
                            for (k in i + 3 until j) {
                                changeText.setSpan(
                                    BackgroundColorSpan(Color.parseColor("#DEDEDE")),
                                    k,
                                    k + 1,
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                ) // i부터 j-1까지 배경을 핑크색으로 바꾼다. (코드 배경)
                            }
                            break
                        }
                    }

                }

                if (i <= targetStr.length - 2 && targetStr[i] == '*' && targetStr[i + 1] == '*' && !boldArr.contains(
                        i
                    )
                ) { // 연속으로 두 개(**) 나왔다면, 여는 부분
                    for (j in i + 2..targetStr.length - 2) {
                        if (targetStr[j] == '*' && targetStr[j + 1] == '*') { // 연속으로 세 개(**) 나왔다면, 닫는 부분
                            boldCnt += 2
                            boldArr.add(i)
                            boldArr.add(j) // bold 처리된 부분에서 *를 지우기 위해 *인 부분을 한 쌍씩 넣는다.
                            for (k in i + 2 until j) {
                                changeText.setSpan(
                                    StyleSpan(Typeface.BOLD),
                                    k,
                                    k + 1,
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                ) // i부터 j-1까지 글씨체를 진하게(bold) 바꾼다.
                            }
                            break
                        }
                    }
                }
            }

            for (i in 0 until changeText.length - (graveCnt * 3) - (boldCnt * 2)) {
                if (changeText[i] == '`' && changeText[i + 1] == '`' && changeText[i + 2] == '`') {
                    changeText.replace(i, i + 3, "")
                }
                if (changeText[i] == '*' && changeText[i + 1] == '*') {
                    changeText.replace(i, i + 2, "")
                }
            }
            if (changeText[changeText.length - 1] == '`' && changeText[changeText.length - 2] == '`' && changeText[changeText.length - 3] == '`') {
                changeText.replace(changeText.length - 3, changeText.length, "")
            }
            if (changeText[changeText.length - 1] == '*' && changeText[changeText.length - 2] == '*') {
                changeText.replace(changeText.length - 2, changeText.length, "")
            }


            tvContent.text = changeText
            itemView.setOnClickListener {
                Toast.makeText(it.context, "Title is $tvTitle", Toast.LENGTH_SHORT).show()
            }
            val serverHashTagStr = showQuestionData.hashtag
            val tags = mutableListOf<String>()
            for (i in serverHashTagStr.split("#")) {
                if (i.isNotEmpty()) {
                    tags.add(i)
                }
            }
            var tagCnt = 1
            for (tag in tags) {
                when (tagCnt) {
                    1 -> {
                        tag1.text = "#".plus(tag)
                        tagCnt++
                    }
                    2 -> {
                        tag2.text = "#".plus(tag)
                        tagCnt++
                    }
                    3 -> {
                        tag3.text = "#".plus(tag)
                    }
                }
            }
            when (tagCnt) {
                1 -> {
                    tag1.text = ""
                    tag2.text = ""
                    tag3.text = ""
                }
                2 -> {
                    tag2.text = ""
                    tag3.text = ""
                }
                3 -> {
                    tag3.text = ""
                }
            }
            tagCnt = 1 // 다시 tagCnt를 1로 초기화
            tags.clear()
        }
    }
}