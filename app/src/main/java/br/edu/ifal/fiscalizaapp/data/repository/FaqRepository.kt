package br.edu.ifal.fiscalizaapp.data.repository

import br.edu.ifal.fiscalizaapp.data.remote.ModelService

class FaqRepository (private val modelService: ModelService) {
    suspend fun getFaq() = modelService.getFaq()
}