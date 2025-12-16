package br.edu.ifal.fiscalizaapp.data.repository

import br.edu.ifal.fiscalizaapp.data.api.faq.FaqAPI

class FaqRepository (private val faqAPI: FaqAPI) {
    suspend fun getFaq() = faqAPI.getFaq()
}