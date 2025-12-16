package br.edu.ifal.fiscalizaapp.model

import com.google.gson.annotations.SerializedName

data class FaqItem (
    val id: Int,
    @SerializedName("question")
    val question: String,
    @SerializedName("answer")
    val answer: String
)