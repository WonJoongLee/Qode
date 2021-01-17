package com.example.qode.recyclerview

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.qode.ContentActivity
import com.example.qode.R

class QuestionTitleAdapter(private val context : Context) :
    RecyclerView.Adapter<QuestionTitleViewHolder>() {

    var data = mutableListOf<ShowQuestionData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionTitleViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_questions, parent, false)

        return QuestionTitleViewHolder(view)
    }

    override fun getItemCount(): Int = data.size
    override fun onBindViewHolder(holder: QuestionTitleViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, ContentActivity::class.java) // 여기서 contentActivity로 넘겨줄 때 title, 댓글 등 정보들을 서버로부터 받아와서 같이 넘겨주면 될 것 같다.
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(holder.itemView.context, intent, null)
        }

    }
}

class QuestionTitleViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
    private val tvContent = itemView.findViewById<TextView>(R.id.tvContent)

    fun bind(showQuestionData: ShowQuestionData){
        tvTitle.text = showQuestionData.tvTitle
        tvContent.text = showQuestionData.tvContent
        itemView.setOnClickListener{
            Toast.makeText(it.context, "Title is $tvTitle", Toast.LENGTH_SHORT).show()
        }
    }


}