package com.example.qode.recyclerview

import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qode.R

class AnswerAdapter(private val context: Context) :
    RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder>() {

    var data = mutableListOf<AnswerData>()
    var selectedItems: SparseBooleanArray = SparseBooleanArray()
    var prePosition = -1
    private lateinit var mListener: OnItemClickListener // 리스너 객체 참조를 저장하는 변수

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_answer, parent, false)

        return AnswerViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        holder.bind(data[position], position, context, mListener)
    }

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int)
    }

    //OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드드
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mListener = listener
    }

    class AnswerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val answerString = itemView.findViewById<TextView>(R.id.answer) // 사용자가 답변한 String
        private val commentViewCnt = itemView.findViewById<TextView>(R.id.commentViewCnt) // 댓글 수
        private val answerWriter = itemView.findViewById<TextView>(R.id.answerWriter) // 답변 작성자
        private val recoCnt = itemView.findViewById<TextView>(R.id.recoCnt) // 답변 추천 수
        private val thumbsUp = itemView.findViewById<ImageButton>(R.id.thumbs_up) // 좋아요 표시
        private val thumbsDown = itemView.findViewById<ImageButton>(R.id.thumbs_down) // 싫어요 표시
        private val commentButton = itemView.findViewById<ImageButton>(R.id.commentImageButton)
        private val commentList = itemView.findViewById<RecyclerView>(R.id.commentRV)
        private val writeAnswerButton = itemView.findViewById<ImageButton>(R.id.writeAnswer)
        //private lateinit var mListener : AnswerViewHolder.OnItemClickListener // 리스너 객체 참조를 저장하는 변수

        //private var bottomView = itemView.findViewById<ConstraintLayout>(R.id.bottomWriterCmtConstraint)
        fun bind(answerData: AnswerData, pos: Int, context: Context, mListener : OnItemClickListener) {
            var thumbsUpClicked = false // Thumbs Up button이 안눌러진 상태면 false, 눌러진 상태면 true다.
            var thumbsDownClicked = false // Thumbs Down button이 안눌러진 상태면 false, 눌러진 상태면 true다.
            var commentClicked =
                false // comment 클릭 여부 확인 위한 변수. 근데 다른 방법으로 클릭 여부를 판단해서 지금은 쓸 당장은 없음.
            answerString.text = answerData.answer
            commentViewCnt.text = answerData.comment
            answerWriter.text = answerData.writer
            recoCnt.text = answerData.recoCnt

            /*아래 리사이클러 뷰는 댓글 리사이클러 뷰*/
            val adapter = CommentAdapter(context)
            val recyclerView = itemView.findViewById<RecyclerView>(R.id.commentRV)
            val commentData = mutableListOf<CommentData>()
            recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            //아래 데이터는 서버에서부터 불러와서 추가해주면 된다. pos를 받으니 pos에 해당하는 서버 주소 확인 후 값들을 가져오면 될 것 같다
            commentData.add(CommentData("Kotlin이 Java보다 좋군요. 좋은 정보 감사합니다. - 다빈치"))
            commentData.add(CommentData("오늘 날씨가 좋네요. - 맥아더"))
            commentData.add(CommentData("좋은 답변 감사합니다. 행복한 하루 되세요. - 네이마르"))
            commentData.add(CommentData("댓글테스트 - 도널드 트럼프"))
            recyclerView.adapter = adapter
            adapter.commentData = commentData
            adapter.notifyDataSetChanged()

            writeAnswerButton.setOnClickListener {
                val inputMethodManager =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(it, pos)

                }
            }

            thumbsUp.setOnClickListener {
                thumbsUpClicked = if (!thumbsUpClicked) { // 클릭되지 않은 상황이라면
                    thumbsUp.setImageResource(R.drawable.ic_baseline_thumb_up_24_yellow) // Thumbs UP을 노란색으로 바꾸고
                    true // 클릭되었으니 thumbsUpClicked를 true값으로 처리한다.
                } else { // 클릭 된 상황이라면
                    thumbsUp.setImageResource(R.drawable.ic_baseline_thumb_up_24_gray) // 회색으로 바꿔 클릭 처리를 해제하고
                    false // 클릭된 상황이 아니므로 false 처리한다.
                }
            }
            thumbsDown.setOnClickListener {
                thumbsDownClicked = if (!thumbsDownClicked) { // 클릭되지 않은 상황이라면
                    thumbsDown.setImageResource(R.drawable.ic_baseline_thumb_down_24_yellow) // 노란색으로 표시해서 활성화시킨다.
                    true//클릭된 상황이므로 true처리한다.
                } else { // 클릭 된 상황이라면
                    thumbsDown.setImageResource(R.drawable.ic_baseline_thumb_down_24_gray) // 회색으로 표시해서 다시 비활성화 시킨다.
                    false// 클릭된 상황이 아니므로 false 처리한다.
                }
            }
            commentButton.setOnClickListener { // 댓글 버튼 클릭 시 댓글 보이도록 설정
                if (commentList.visibility == View.GONE) {
                    commentList.visibility = View.VISIBLE
                } else if (commentList.visibility == View.VISIBLE) {
                    commentList.visibility = View.GONE
                }
            }
        }
    }
}
