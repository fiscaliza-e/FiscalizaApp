package br.edu.ifal.fiscalizaapp.data.repository

import br.edu.ifal.fiscalizaapp.data.remote.CepService

class CepRepository(private val cepService: CepService) {
    suspend fun getAddressByCep(cep: String) = cepService.getAddressByCep(cep)
}