package com.example.qode.recyclerview

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.SharedPreferences
import android.os.Looper
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
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class QuestionTitleAdapter(private val context: Context) :

    RecyclerView.Adapter<QuestionTitleViewHolder>() {

    var data = mutableListOf<ShowQuestionData>()

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
            val posBoardUrl = serverString.plus("board/").plus((position + 1).toString())
            //var (title, content, viewCnt, reco, hashtag, createdTime, writer) : String = ""
            //startActivity(holder.itemView.context, intent, null)
            startActivity(context,intent.addFlags(FLAG_ACTIVITY_NEW_TASK), null)


            /** 서버 연결할 때 이 부분 주석 해제 해주면 된다.
            GlobalScope.launch {
                val data = getDataFromServer(posBoardUrl)
                println(data["title"]) // 값을 Map으로 받는다. key에는 title, value에는 title string값
                Log.e("posBoardURL", posBoardUrl)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("title", data["title"])
                intent.putExtra("content", data["content"])
                intent.putExtra("viewCnt", data["viewCnt"])
                intent.putExtra("reco", data["reco"])
                intent.putExtra("hashtag", data["hashtag"])
                intent.putExtra("createdTime", data["createdTime"])
                intent.putExtra("writer", data["writer"])
                intent.putExtra("boardNum", data["boardNum"]) // 첫 번째 parameter는 넘겨줄 이름, 두 번째 parameter는 data에 저장된 key로 접근해서 실제 value가져옴
                startActivity(holder.itemView.context, intent, null) // 새로운 activity로 이동
            }
            */
        }
    }

    private suspend fun getDataFromServer(posBoardUrl: String) =
        suspendCoroutine<MutableMap<String, String>> {
            android.os.Handler(Looper.getMainLooper()).postDelayed({
                //var arr = ArrayList<Pair<String, String>>()
                var arr = mutableMapOf<String, String>()
                //val arr = mutableListOf<Map<String, String>>()
                val request = object : StringRequest(Method.GET, posBoardUrl,
                    Response.Listener { response ->
                        val jsonObject = JSONObject(response)
                        arr["title"] = jsonObject.getString("bbsTitle")
                        arr["content"] = jsonObject.getString("bbsContent")
                        arr["viewCnt"] = jsonObject.getString("bbsViews")
                        arr["reco"] = jsonObject.getString("bbsReco")
                        arr["hashtag"] = jsonObject.getString("hashTagContent")
                        arr["createdTime"] = jsonObject.getString("created_at")
                        arr["writer"] = jsonObject.getString("boarder")
                        arr["boardNum"] = jsonObject.getString("id")
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

    fun bind(showQuestionData: ShowQuestionData) {
        tvTitle.text = showQuestionData.tvTitle
        tvContent.text = showQuestionData.tvContent
        itemView.setOnClickListener {
            Toast.makeText(it.context, "Title is $tvTitle", Toast.LENGTH_SHORT).show()
        }
    }
}