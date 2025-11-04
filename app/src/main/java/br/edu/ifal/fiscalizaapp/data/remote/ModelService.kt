package br.edu.ifal.fiscalizaapp.data.remote

import br.edu.ifal.fiscalizaapp.data.model.Protocol
import br.edu.ifal.fiscalizaapp.data.model.User
import retrofit2.http.GET
import retrofit2.http.Query

interface ModelService {
    @GET("protocol")
    suspend fun getProtocols(): List<Protocol>

    @GET("protocol")
    suspend fun getProtocolsByUserId(@Query("userId") userId: Int): List<Protocol>

    @GET("user")
    suspend fun getUsers(): List<User>
}