package br.edu.ifal.fiscalizaapp.data.api.protocol

import br.edu.ifal.fiscalizaapp.model.Protocol
import retrofit2.http.GET
import retrofit2.http.Query

interface ProtocolAPI {
    @GET("protocol")
    suspend fun getProtocols(): List<Protocol>

    @GET("protocol")
    suspend fun getProtocolsByUserId(@Query("userId") userId: Int): List<Protocol>
}