package br.edu.ifal.fiscalizaapp.data.repository

import br.edu.ifal.fiscalizaapp.data.api.protocol.ProtocolAPI

class ProtocolRepository(private val protocolAPI: ProtocolAPI) {

    suspend fun getProtocols() = protocolAPI.getProtocols()

    suspend fun getProtocolsByUserId(userId: Int) = protocolAPI.getProtocolsByUserId(userId)
}