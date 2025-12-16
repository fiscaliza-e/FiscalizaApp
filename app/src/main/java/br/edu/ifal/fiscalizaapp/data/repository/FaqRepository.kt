package br.edu.ifal.fiscalizaapp.data.repository

import br.edu.ifal.fiscalizaapp.data.api.faq.FaqAPI
import br.edu.ifal.fiscalizaapp.data.db.dao.FaqDao
import br.edu.ifal.fiscalizaapp.data.db.entities.FaqEntity
import br.edu.ifal.fiscalizaapp.model.FaqItem

class FaqRepository (
    private val faqAPI: FaqAPI,
    private val faqDao: FaqDao
) {

    suspend fun getFaq(): List<FaqItem> {
        return try {
            val apiResponse = faqAPI.getFaq()

            val entities = apiResponse.map { item ->
                FaqEntity(
                    id = item.id,
                    question = item.question,
                    answer = item.answer
                )
            }

            faqDao.deleteAll()
            faqDao.insertAll(entities)

            apiResponse

        } catch (e: Exception) {
            val localData = faqDao.getAll()

            if (localData.isNotEmpty()) {
                localData.map { entity ->
                    FaqItem(
                        id = entity.id,
                        question = entity.question,
                        answer = entity.answer
                    )
                }
            } else {
                throw Exception("Sem conexão e nenhum dado salvo localmente.")
            }
        }
    }
}