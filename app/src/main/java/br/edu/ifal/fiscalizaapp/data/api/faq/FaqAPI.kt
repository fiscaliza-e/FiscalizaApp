package br.edu.ifal.fiscalizaapp.data.api.faq

import br.edu.ifal.fiscalizaapp.model.FaqItem
import retrofit2.http.GET

interface FaqAPI {
    @GET("faq")
    suspend fun getFaq(): List<FaqItem>
}