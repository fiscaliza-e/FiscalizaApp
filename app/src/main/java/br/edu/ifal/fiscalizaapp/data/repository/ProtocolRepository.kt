package br.edu.ifal.fiscalizaapp.data.repository

import br.edu.ifal.fiscalizaapp.data.api.protocol.ProtocolAPI
import br.edu.ifal.fiscalizaapp.data.db.dao.ProtocolDao
import br.edu.ifal.fiscalizaapp.data.db.entities.ProtocolEntity
import br.edu.ifal.fiscalizaapp.model.Protocol
import kotlinx.coroutines.flow.Flow

class ProtocolRepository(private val protocolAPI: ProtocolAPI, private val protocolDao: ProtocolDao) {

    fun getProtocols(userId: Int): Flow<List<ProtocolEntity>> {
        return protocolDao.getProtocolsByUserId(userId)
    }

    suspend fun refreshProtocols(userId: Int) {
        try {
            val apiProtocols = protocolAPI.getProtocolsByUserId(userId)
            val protocolEntities = apiProtocols.map { it.toEntity() }

            protocolDao.deleteProtocolsByUserId(userId)

            protocolDao.insertAll(protocolEntities)

        } catch (e: Exception) {
            android.util.Log.e("ProtocolRepository", "Erro ao sincronizar protocolos: ${e.message}")
        }
    }

    suspend fun saveProtocol(protocol: ProtocolEntity) {
        protocolDao.insert(protocol)
    }
}

private fun Protocol.toEntity(): ProtocolEntity {
    return ProtocolEntity(
        protocolNumber = this.protocolNumber,
        title = this.title,
        description = this.description,
        status = this.status,
        date = this.date,
        userId = this.userId,
        cep = "",
        rua = "",
        bairro = "",
        numero = "",
        pontoReferencia = "",
        useMyLocation = false
    )
}
