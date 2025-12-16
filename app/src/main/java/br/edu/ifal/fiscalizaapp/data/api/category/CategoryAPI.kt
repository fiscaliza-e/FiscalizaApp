package br.edu.ifal.fiscalizaapp.data.api.category

import br.edu.ifal.fiscalizaapp.model.NetworkCategory
import retrofit2.http.GET

interface CategoryAPI {
    @GET("categories")
    suspend fun getCategories(): List<NetworkCategory>
}