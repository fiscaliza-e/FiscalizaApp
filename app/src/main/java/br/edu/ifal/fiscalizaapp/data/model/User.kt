package br.edu.ifal.fiscalizaapp.data.model

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    val name: String,
    val email: String,
    @SerializedName("zipCode")
    val zipCode: String,
    val cpf: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String
)