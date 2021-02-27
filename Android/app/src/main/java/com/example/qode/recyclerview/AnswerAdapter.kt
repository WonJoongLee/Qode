package com.example.qode.recyclerview

import android.app.Notification
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Looper
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qode.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.logging.Handler
import java.util.logging.LogRecord

class AnswerAdapter(private val context: Context, private val tempArr: IntArray) :
    RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder>() {

    var data = mutableListOf<AnswerData>()
    var selectedItems: SparseBooleanArray = SparseBooleanArray()
    var prePosition = -1
    private lateinit var mListener: OnItemClickListener // 리스너 객체 참조를 저장하는 변수

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_answer, parent, false)

        return AnswerViewHolder(view, tempArr)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        holder.bind(data[position], position, context, mListener)
    }

    interface OnItemClickListener {
        fun onWriteAnswerCommentClick(v: View?, position: Int, answerIdArray: IntArray)
        suspend fun onRecoAddButtonClick(v: View?, position: Int, answerIdArray: IntArray): Boolean
        suspend fun onRecoSubtractButtonClick(
            v: View?,
            position: Int,
            answerIdArray: IntArray
        ): Boolean

        suspend fun oncommentButtonClick(v: View?, position: Int): MutableList<CommentData>
    }

    //OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mListener = listener
    }

    class AnswerViewHolder(itemView: View, private val answerIdArray: IntArray) :
        RecyclerView.ViewHolder(
            itemView
        ) {
        private val answerString = itemView.findViewById<TextView>(R.id.answer) // 사용자가 답변한 String
        private val commentViewCnt = itemView.findViewById<TextView>(R.id.commentViewCnt) // 댓글 수
        private val answerWriter = itemView.findViewById<TextView>(R.id.answerWriter) // 답변 작성자
        private val recoCnt = itemView.findViewById<TextView>(R.id.recoCnt) // 답변 추천 수
        private val thumbsUp = itemView.findViewById<ImageButton>(R.id.answer_thumbs_up) // 좋아요 표시
        private val thumbsDown =
            itemView.findViewById<ImageButton>(R.id.answer_thumbs_down) // 싫어요 표시
        private val commentButton = itemView.findViewById<ImageButton>(R.id.commentImageButton)
        private val commentList = itemView.findViewById<RecyclerView>(R.id.commentRV)
        private val writeAnswerButton = itemView.findViewById<ImageButton>(R.id.writeAnswer)
        private val plzNewCmtTV = itemView.findViewById<TextView>(R.id.plzNewCmtTV)
        private val answerUserNickname = itemView.findViewById<TextView>(R.id.answerWriter)

        //private lateinit var mListener : AnswerViewHolder.OnItemClickListener // 리스너 객체 참조를 저장하는 변수

        //private var bottomView = itemView.findViewById<ConstraintLayout>(R.id.bottomWriterCmtConstraint)
        fun bind(
            answerData: AnswerData,
            pos: Int,
            context: Context,
            mListener: OnItemClickListener
        ) {
            var thumbsUpClicked = false // Thumbs Up button이 안눌러진 상태면 false, 눌러진 상태면 true다.
            var thumbsDownClicked = false // Thumbs Down button이 안눌러진 상태면 false, 눌러진 상태면 true다.
            var commentClicked =
                false // comment 클릭 여부 확인 위한 변수. 근데 다른 방법으로 클릭 여부를 판단해서 지금은 쓸 당장은 없음.
            val pos = adapterPosition

            val targetStr = answerData.answer
            val changeText = SpannableStringBuilder(targetStr)
            val graveArr = mutableListOf<Int>()
            val boldArr = mutableListOf<Int>()
            var graveCnt = 0
            var boldCnt = 0

            answerUserNickname.text = answerData.answerWriter
            recoCnt.text = answerData.recoCnt
            //TODO comment갯수 넣기 commentCount받아와서 넣어주면 된다.

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
                                //graveArr.add(j - 3) // 뒤에 배경이 들어가게 처리된 부분에서 `를 지우기 위해 `인 부분을 한 쌍씩 넣는다.
                                graveArr.add(j) // 뒤에 배경이 들어가게 처리된 부분에서 `를 지우기 위해 `인 부분을 한 쌍씩 넣는다.
                                println("$$$ i : $i, j : $j")
                                for (k in i + 3 until j) {
                                    changeText.setSpan(
                                        BackgroundColorSpan(Color.parseColor("#fbe4e4")),
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

                println("graveCnt : ${graveCnt}, boldCnt : ${boldCnt} ,total length ${changeText.length - (graveCnt * 3) - (boldCnt * 2)}")
                for (i in 0 until changeText.length - (graveCnt * 3) - (boldCnt * 2)) {
                    if (changeText[i] == '`' && changeText[i + 1] == '`' && changeText[i + 2] == '`') {
                        println("i : $i @@@")
                        changeText.replace(i, i + 3, "")
                    }
                    if (changeText[i] == '*' && changeText[i + 1] == '*') {
                        println("i : $i @@@")
                        changeText.replace(i, i + 2, "")
                    }
                }
                if(changeText[changeText.length-1] == '`' && changeText[changeText.length-2] == '`' && changeText[changeText.length-3] == '`'){
                    changeText.replace(changeText.length-3, changeText.length, "")
                }
                if(changeText[changeText.length-1] == '*' && changeText[changeText.length-2] == '*'){
                    changeText.replace(changeText.length-2, changeText.length, "")
                }
            }
            answerString.text = changeText



            val serverUrl =
                context.getSharedPreferences("sharedPrefs", AppCompatActivity.MODE_PRIVATE)
                    .getString(
                        "serverUrl",
                        null
                    )
            val userid = context.getSharedPreferences("sharedPrefs", AppCompatActivity.MODE_PRIVATE)
                .getString(
                    "userid",
                    null
                )

//            /*아래 리사이클러 뷰는 댓글 리사이클러 뷰*/
//            val adapter = CommentAdapter(context)
//            val recyclerView = itemView.findViewById<RecyclerView>(R.id.commentRV)
//            val commentData = mutableListOf<CommentData>()
//            recyclerView.layoutManager =
//                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            //아래 데이터는 서버에서부터 불러와서 추가해주면 된다. pos를 받으니 pos에 해당하는 서버 주소 확인 후 값들을 가져오면 될 것 같다


//            commentData.add(CommentData("Kotlin이 Java보다 좋군요. 좋은 정보 감사합니다. - 다빈치"))
//            commentData.add(CommentData("오늘 날씨가 좋네요. - 맥아더"))
//            commentData.add(CommentData("좋은 답변 감사합니다. 행복한 하루 되세요. - 네이마르"))
//            commentData.add(CommentData("댓글테스트 - 도널드 트럼프"))

            commentButton.setOnClickListener { // 댓글 버튼 클릭 시 댓글 보이도록 설정
                /*아래 리사이클러 뷰는 댓글 리사이클러 뷰*/
                val adapter = CommentAdapter(context)
                val recyclerView = itemView.findViewById<RecyclerView>(R.id.commentRV)
                val commentData = mutableListOf<CommentData>()
                recyclerView.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                if (commentList.visibility == View.GONE) {
                    commentList.visibility = View.VISIBLE
                    var isComment = false
                    GlobalScope.launch {
                        val commentStringData = mListener.oncommentButtonClick(itemView, pos)
                        if (commentStringData.isNotEmpty()) {
                            isComment = true
                            for (i in commentStringData) {
                                commentData.add(i)
                            }
                        }
                    }
                    android.os.Handler(Looper.getMainLooper()).postDelayed({
                        //commentData.add(CommentData("Kotlin이 Java보다 좋군요. 좋은 정보 감사합니다. - 다빈치"))
                        if (!isComment) plzNewCmtTV.visibility =
                            View.VISIBLE // 댓글이 없을 때 댓글을 새로 달아달라는 textview를 보여준다.
                        recyclerView.adapter = adapter
                        adapter.commentData = commentData
                        adapter.notifyDataSetChanged()
                        println(commentData)
                    }, 700) // 댓글을 불러오는데 0.5초 정도 걸린다.
                } else if (commentList.visibility == View.VISIBLE) {
                    commentList.visibility = View.GONE
                    plzNewCmtTV.visibility = View.GONE
                }
            }


//            recyclerView.adapter = adapter
//            adapter.commentData = commentData
//            adapter.notifyDataSetChanged()

            writeAnswerButton.setOnClickListener {
                val inputMethodManager =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

                if (pos != RecyclerView.NO_POSITION) {
                    mListener.onWriteAnswerCommentClick(it, pos, answerIdArray)
                }
            }

            thumbsUp.setOnClickListener {
                thumbsUpClicked = if (!thumbsUpClicked) { // 클릭되지 않은 상황이라면
                    if (pos != RecyclerView.NO_POSITION) {
                        GlobalScope.launch {
                            val temp = mListener.onRecoAddButtonClick(it, pos, answerIdArray)
                            Log.e("눌렀는지", temp.toString()) // true면 이미 눌름
                            if (!temp) {  // 처음 누르는 경우
                                thumbsUp.setImageResource(R.drawable.ic_baseline_thumb_up_24_yellow) // Thumbs UP을 노란색으로 바꾸고
                            }
                        }
                    }
                    true // 클릭되었으니 thumbsUpClicked를 true값으로 처리한다.
                } else { // 클릭 된 상황이라면
                    thumbsUp.setImageResource(R.drawable.ic_baseline_thumb_up_24_gray) // 회색으로 바꿔 클릭 처리를 해제하고
                    false // 클릭된 상황이 아니므로 false 처리한다.
                }
            }
            thumbsDown.setOnClickListener {
                thumbsDownClicked = if (!thumbsDownClicked) { // 클릭되지 않은 상황이라면

                    if (pos != RecyclerView.NO_POSITION) {
                        GlobalScope.launch {
                            val temp = mListener.onRecoSubtractButtonClick(it, pos, answerIdArray)
                            if (!temp) {  // 처음 누르는 경우
                                thumbsDown.setImageResource(R.drawable.ic_baseline_thumb_down_24_yellow) // Thumbs UP을 노란색으로 바꾸고
                            }
                        }
                    }
                    true//클릭된 상황이므로 true처리한다.
                } else { // 클릭 된 상황이라면
                    thumbsDown.setImageResource(R.drawable.ic_baseline_thumb_down_24_gray) // 회색으로 표시해서 다시 비활성화 시킨다.
                    false// 클릭된 상황이 아니므로 false 처리한다.
                }
            }

        }


    }
}
