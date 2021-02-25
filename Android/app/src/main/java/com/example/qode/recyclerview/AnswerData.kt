package com.example.qode.recyclerview
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AnswerData (
    val answer : String, // 답변 내용
    val writer : String, // 작성자
    val comment : String, // 댓글 수
    val recoCnt : String, // 답변 추천 수
    val answerId : String // answer id
) : Parcelable