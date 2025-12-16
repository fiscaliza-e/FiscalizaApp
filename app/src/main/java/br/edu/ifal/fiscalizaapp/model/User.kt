package br.edu.ifal.fiscalizaapp.model

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    val name: String,
    val email: String,
    @SerializedName("zipCode")
    val zipCode: String,
    val cpf: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,

    val password: String,
    val agreeToTerms: Boolean
)