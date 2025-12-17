package br.edu.ifal.fiscalizaapp.data.api.user

import br.edu.ifal.fiscalizaapp.data.api.dto.NetworkUser
import retrofit2.http.GET
import retrofit2.http.Path

interface UserAPI {
    @GET("user")
    suspend fun getUsers(): List<NetworkUser>

    @GET("user/{id}")
    suspend fun getUserById(@Path("id") id: Int): NetworkUser
}