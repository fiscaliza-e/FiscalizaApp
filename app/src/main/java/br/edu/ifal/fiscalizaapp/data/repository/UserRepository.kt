package br.edu.ifal.fiscalizaapp.data.repository

import br.edu.ifal.fiscalizaapp.data.remote.ModelService

class UserRepository(private val modelService: ModelService) {

    suspend fun getUsers() = modelService.getUsers()
}