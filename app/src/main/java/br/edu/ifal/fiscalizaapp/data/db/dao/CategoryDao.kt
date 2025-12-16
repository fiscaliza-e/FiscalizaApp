package br.edu.ifal.fiscalizaapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.edu.ifal.fiscalizaapp.data.db.entities.CategoryEntity

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories")
    suspend fun getAll(): List<CategoryEntity>

    @Insert
    suspend fun insertAll(categories: List<CategoryEntity>)

    @Query("SELECT COUNT(*) FROM categories")
    suspend fun getCount(): Int
}