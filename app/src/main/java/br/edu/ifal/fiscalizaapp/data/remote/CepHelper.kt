package br.edu.ifal.fiscalizaapp.data.remote

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

    fun getCepService(): CepService {
        return getInstance().create(CepService::class.java)
    }
}