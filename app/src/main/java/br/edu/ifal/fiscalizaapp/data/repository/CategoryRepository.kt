package br.edu.ifal.fiscalizaapp.data.repository

import br.edu.ifal.fiscalizaapp.data.api.category.CategoryAPI


open class CategoryRepository(private val categoryAPI: CategoryAPI) {
    open suspend fun getCategories() = categoryAPI.getCategories()
}