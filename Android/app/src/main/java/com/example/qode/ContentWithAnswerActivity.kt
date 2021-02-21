package com.example.qode

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
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
import org.json.JSONArray

class ContentWithAnswerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContentWithAnswerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var thumbsUpClicked = false // Thumbs Up button이 안눌러진 상태면 false, 눌러진 상태면 true다.
        var thumbsDownClicked = false // Thumbs Down button이 안눌러진 상태면 false, 눌러진 상태면 true다.
        val serverString =
            getSharedPreferences("sharedPrefs", MODE_PRIVATE).getString("serverUrl", null)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_content_with_answer)

        val answerDataList = mutableListOf<AnswerData>()
        val answerRecyclerView = findViewById<RecyclerView>(R.id.answerRecyclerView)
        val adapter = AnswerAdapter(applicationContext)
        answerRecyclerView.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        answerDataList.add(
            AnswerData(
                "가장 큰 차이라면 cout은 출력하려는 값의 형(type)을 컴파일러가 검사할 수 있는 반면에 printf()는 그렇지 못하다는 것입니다. \n",
                "홍길동",
                "1",
                "2"
            )
        )
        answerDataList.add(AnswerData("Kotlin은 세계 최고의 상용 개발 도", "강태공", "3", " -2"))
        answerDataList.add(
            AnswerData(
                "2018년 카카오가 카카오톡 메시징 서버에 Kotlin을 사용해 본 결과, 코드량이 비약적으로 감소하고 생산성이 대폭 향상되었다고 한다.",
                "오카카",
                "0",
                "4"
            )
        )
        answerDataList.add(
            AnswerData(
                "장황했던 Java와 비교하면 눈물날 정도로 간결한 문법을 제공한다. 간결한 문법을 제공하면서도 런타임 오버헤드가 거의 없다.",
                "네이버",
                "4",
                "3"
            )
        )
        answerDataList.add(
            AnswerData(
                "예외처리를 강제하지 않는다. JSONObject를 만들 때 눈물이 난다...",
                "오바마",
                "0",
                " -10"
            )
        )
        answerRecyclerView.adapter = adapter
        answerRecyclerView.setHasFixedSize(false) // ?
        adapter.data = answerDataList
        adapter.notifyDataSetChanged()


        adapter.setOnItemClickListener(object : AnswerAdapter.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                binding.commentET.text.clear()
                binding.commentET.hint = "댓글을 입력해주세요."
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
                binding.thumbsUp.setImageResource(R.drawable.ic_baseline_thumb_up_24_yellow) // Thumbs UP을 노란색으로 바꾸고
                true // 클릭되었으니 thumbsUpClicked를 true값으로 처리한다.
            } else { // 클릭 된 상황이라면
                binding.thumbsUp.setImageResource(R.drawable.ic_baseline_thumb_up_24_gray) // 회색으로 바꿔 클릭 처리를 해제하고
                false // 클릭된 상황이 아니므로 false 처리한다.
            }
        }
        binding.thumbsDown.setOnClickListener {
            thumbsDownClicked = if (!thumbsDownClicked) { // 클릭되지 않은 상황이라면
                binding.thumbsDown.setImageResource(R.drawable.ic_baseline_thumb_down_24_yellow) // Thumbs UP을 노란색으로 바꾸고
                true // 클릭되었으니 thumbsUpClicked를 true값으로 처리한다.
            } else { // 클릭 된 상황이라면
                binding.thumbsDown.setImageResource(R.drawable.ic_baseline_thumb_down_24_gray) // 회색으로 바꿔 클릭 처리를 해제하고
                false // 클릭된 상황이 아니므로 false 처리한다.
            }
        }



        binding.sendAnswerImageButton.setOnClickListener {
            val request = object : StringRequest(Method.POST, serverString,
                Response.Listener { response ->
                    val jsonArray = JSONArray(response)
                    val commentStr = binding.commentET.text

                }, Response.ErrorListener { error ->
                    Log.e("ERROR", "@@@@ + $error")
                }) {
                // val params //TODO 여기서 서버로 보내줘야 한다.
            }
            val queue = Volley.newRequestQueue(applicationContext)

        }
    }
}