package br.edu.ifal.fiscalizaapp.data.remote

import br.edu.ifal.fiscalizaapp.data.remote.dto.CepResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CepService {
    @GET("ws/{cep}/json/")
    suspend fun getAddressByCep(@Path("cep") cep: String): CepResponse
}