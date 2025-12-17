package br.edu.ifal.fiscalizaapp.data.api.dto

import com.google.gson.annotations.SerializedName

data class NetworkUser(
    val id: Int,
    val name: String,
    val email: String,
    @SerializedName("zipCode") val zipCode: String? = null,
    val password: String,
    val agreeToTerms: Boolean,
    val cpf: String,
    @SerializedName("phoneNumber") val phoneNumber: String? = null,
    @SerializedName("pictureUrl") val pictureUrl: String? = null
)


