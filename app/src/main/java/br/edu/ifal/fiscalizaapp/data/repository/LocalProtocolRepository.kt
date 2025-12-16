package br.edu.ifal.fiscalizaapp.data.repository

import br.edu.ifal.fiscalizaapp.data.db.entities.ProtocolEntity
import br.edu.ifal.fiscalizaapp.data.db.dao.ProtocolDao

open class LocalProtocolRepository (private val protocolDao: ProtocolDao) {

    open suspend fun saveProtocol(protocol: ProtocolEntity) {
        protocolDao.insert(protocol)
    }

    open suspend fun getAllProtocols(): List<ProtocolEntity> {
        return protocolDao.getAll()
    }
}