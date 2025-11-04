package br.edu.ifal.fiscalizaapp.data.model

import com.google.gson.annotations.SerializedName

data class Protocol(
    val title: String,
    val description: String,
    val status: String,
    @SerializedName("protocolNumber")
    val protocolNumber: String,
    val date: String,
    val userId: Int
)