package br.edu.ifal.fiscalizaapp.data.repository

import br.edu.ifal.fiscalizaapp.data.api.cep.CepAPI

class CepRepository(private val cepAPI: CepAPI) {
    suspend fun getAddressByCep(cep: String) = cepAPI.getAddressByCep(cep)
}