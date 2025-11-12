package br.edu.ifal.fiscalizaapp.data.repository

import br.edu.ifal.fiscalizaapp.data.remote.ModelService

open class CategoryRepository(private val modelService: ModelService) {
    open suspend fun getCategories() = modelService.getCategories()
}