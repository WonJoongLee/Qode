package com.example.qode.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
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