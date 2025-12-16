package br.edu.ifal.fiscalizaapp.data.api.cep

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CepRetrofitHelper {

    private const val BASE_URL = "https://viacep.com.br/"

    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getCepAPI(): CepAPI {
        return getInstance().create(CepAPI::class.java)
    }
}