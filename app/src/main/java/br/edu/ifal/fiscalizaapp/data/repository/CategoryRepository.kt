package br.edu.ifal.fiscalizaapp.data.repository

import br.edu.ifal.fiscalizaapp.R
import br.edu.ifal.fiscalizaapp.data.api.category.CategoryAPI
import br.edu.ifal.fiscalizaapp.data.db.dao.CategoryDao
import br.edu.ifal.fiscalizaapp.data.db.entities.CategoryEntity

class CategoryRepository(
    private val categoryAPI: CategoryAPI,
    private val categoryDao: CategoryDao
) {
    suspend fun getCategories(): List<CategoryEntity> {
        return try {
            val apiResponse = categoryAPI.getCategories()

            val entities = apiResponse.map { networkCat ->
                val androidIconId = getDrawableIdByTitle(networkCat.title)

                CategoryEntity(
                    id = networkCat.id,
                    title = networkCat.title,
                    description = networkCat.description,
                    iconResId = androidIconId
                )
            }

            if (entities.isNotEmpty()) {
                categoryDao.deleteAll()
                categoryDao.insertAll(entities)
            }

            categoryDao.getAll()

        } catch (e: Exception) {
            val local = categoryDao.getAll()
            local.ifEmpty { throw e }
        }
    }

    private fun getDrawableIdByTitle(title: String?): Int {
        return when (title?.lowercase()?.trim()) {
            "iluminação", "iluminação pública" -> R.drawable.ic_poste
            "pavimentação" -> R.drawable.ic_pavimentacao
            "saneamento" -> R.drawable.ic_saneamento
            "órgãos públicos" -> R.drawable.ic_orgaos_publicos
            "transporte coletivo" -> R.drawable.ic_transporte_coletivo
            "espaço público" -> R.drawable.ic_espaco_publico
            "trânsito" -> R.drawable.ic_semaforo_apagado
            "outros" -> R.drawable.ic_outras_categorias
            else -> R.drawable.ic_outras_categorias
        }
    }
}