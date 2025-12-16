package br.edu.ifal.fiscalizaapp.data.api.cep

import br.edu.ifal.fiscalizaapp.data.api.dto.CepResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CepAPI {
    @GET("ws/{cep}/json/")
    suspend fun getAddressByCep(@Path("cep") cep: String): CepResponse
}