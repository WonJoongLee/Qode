package com.example.qode.recyclerview

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.qode.R

class AnswerAdapter(private val context: Context) : RecyclerView.Adapter<AnswerViewHolder>() {

    var data = mutableListOf<AnswerData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_answer, parent, false)



        return AnswerViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        holder.bind(data[position])

    }
}

class AnswerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val answerString = itemView.findViewById<TextView>(R.id.answer) // 사용자가 답변한 String
    private val commentViewCnt = itemView.findViewById<TextView>(R.id.commentViewCnt) // 댓글 수
    private val answerWriter = itemView.findViewById<TextView>(R.id.answerWriter) // 답변 작성자
    private val recoCnt = itemView.findViewById<TextView>(R.id.recoCnt) // 답변 추천 수
    private val thumbsUp = itemView.findViewById<ImageButton>(R.id.thumbs_up) // 좋아요 표시
    private val thumbsDown = itemView.findViewById<ImageButton>(R.id.thumbs_down) // 싫어요 표시
    //private var bottomView = itemView.findViewById<ConstraintLayout>(R.id.bottomWriterCmtConstraint)
    fun bind(answerData: AnswerData) {
        var thumbsUpClicked = false // Thumbs Up button이 안눌러진 상태면 false, 눌러진 상태면 true다.
        var thumbsDownClicked = false // Thumbs Down button이 안눌러진 상태면 false, 눌러진 상태면 true다.
        answerString.text = answerData.answer
        commentViewCnt.text = answerData.comment
        answerWriter.text = answerData.writer
        recoCnt.text = answerData.recoCnt
        thumbsUp.setOnClickListener {
            thumbsUpClicked = if(!thumbsUpClicked){ // 클릭되지 않은 상황이라면
                thumbsUp.setImageResource(R.drawable.ic_baseline_thumb_up_24_yellow) // Thumbs UP을 노란색으로 바꾸고
                true // 클릭되었으니 thumbsUpClicked를 true값으로 처리한다.
            }else{ // 클릭 된 상황이라면
                thumbsUp.setImageResource(R.drawable.ic_baseline_thumb_up_24_gray) // 회색으로 바꿔 클릭 처리를 해제하고
                false // 클릭된 상황이 아니므로 false 처리한다.
            }
        }
        thumbsDown.setOnClickListener {
            thumbsDownClicked = if(!thumbsDownClicked){ // 클릭되지 않은 상황이라면
                thumbsDown.setImageResource(R.drawable.ic_baseline_thumb_down_24_yellow) // 노란색으로 표시해서 활성화시킨다.
                true//클릭된 상황이므로 true처리한다.
            }else{ // 클릭 된 상황이라면
                thumbsDown.setImageResource(R.drawable.ic_baseline_thumb_down_24_gray) // 회색으로 표시해서 다시 비활성화 시킨다.
                false// 클릭된 상황이 아니므로 false 처리한다.
            }

        }
    }
}