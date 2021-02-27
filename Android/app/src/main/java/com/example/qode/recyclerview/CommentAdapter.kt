package com.example.qode.recyclerview

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qode.R

class CommentAdapter(private val context: Context) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    var commentData = mutableListOf<CommentData>()
    private lateinit var mListener: CommentAdapter.OnItemClickListener // 리스너 객체 참조를 저장하는 변수

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(commentData[position])
    }

    interface OnItemClickListener {
        fun onCommentClick(v: View?, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mListener = listener
    }

    override fun getItemCount(): Int = commentData.size

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val commentString = itemView.findViewById<TextView>(R.id.commentContent)
        private val commentWriterString = itemView.findViewById<TextView>(R.id.commentWriter)
        fun bind(commentData: CommentData) {
            commentWriterString.text = commentData.commentWriter


            val targetStr = commentData.comment
            val changeText = SpannableStringBuilder(targetStr)
            val graveArr = mutableListOf<Int>()
            val boldArr = mutableListOf<Int>()
            var graveCnt = 0
            var boldCnt = 0

            if (targetStr.length >= 3) {
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
                if (changeText[changeText.length - 1] == '`' && changeText[changeText.length - 2] == '`' && changeText[changeText.length - 3] == '`') {
                    changeText.replace(changeText.length - 3, changeText.length, "")
                }
                if (changeText[changeText.length - 1] == '*' && changeText[changeText.length - 2] == '*') {
                    changeText.replace(changeText.length - 2, changeText.length, "")
                }
            }
            commentString.text = changeText

        }
    }
}