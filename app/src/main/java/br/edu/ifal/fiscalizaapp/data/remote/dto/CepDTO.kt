package br.edu.ifal.fiscalizaapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CepResponse(
    @SerializedName("cep") val zipCode: String,
    @SerializedName("logradouro") val street: String,
    @SerializedName("complemento") val complement: String,
    @SerializedName("bairro") val neighborhood: String,
    @SerializedName("localidade") val city: String,
    @SerializedName("uf") val state: String,
    @SerializedName("ibge") val ibgeCode: String,
    @SerializedName("gia") val gia: String,
    @SerializedName("ddd") val ddd: String,
    @SerializedName("siafi") val siafi: String,
    @SerializedName("erro") val error: Boolean? = false
)