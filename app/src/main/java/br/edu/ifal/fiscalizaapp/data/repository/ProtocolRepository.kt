package br.edu.ifal.fiscalizaapp.data.repository

import br.edu.ifal.fiscalizaapp.data.remote.ModelService

class ProtocolRepository(private val modelService: ModelService) {

    suspend fun getProtocols() = modelService.getProtocols()

    suspend fun getProtocolsByUserId(userId: Int) = modelService.getProtocolsByUserId(userId)
}