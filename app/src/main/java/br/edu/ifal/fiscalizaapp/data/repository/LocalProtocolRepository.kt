package br.edu.ifal.fiscalizaapp.data.repository

import br.edu.ifal.fiscalizaapp.model.ProtocolEntity
import br.edu.ifal.fiscalizaapp.db.ProtocolDao

open class LocalProtocolRepository (private val protocolDao: ProtocolDao) {

    open suspend fun saveProtocol(protocol: ProtocolEntity) {
        protocolDao.insertProtocol(protocol)
    }

    open suspend fun getAllProtocols(): List<ProtocolEntity> {
        return protocolDao.getAllProtocols()
    }
}