package br.edu.ifal.fiscalizaapp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.edu.ifal.fiscalizaapp.db.entities.CategoryEntity

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<CategoryEntity>

    @Insert
    suspend fun insertAllCategories(categories: List<CategoryEntity>)

    @Query("SELECT COUNT(*) FROM categories")
    suspend fun getCategoriesCount(): Int
}