package com.example.qode.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qode.R

class CommentAdapter(private val context : Context) :RecyclerView.Adapter<CommentAdapter.CommentViewHolder>(){

    var commentData = mutableListOf<CommentData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(commentData[position])
    }

    override fun getItemCount(): Int = commentData.size

    class CommentViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        private val commentString = itemView.findViewById<TextView>(R.id.tempComment)
        fun bind(commentData : CommentData){
            commentString.text = commentData.comment
        }
    }
}