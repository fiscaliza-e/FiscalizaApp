package br.edu.ifal.fiscalizaapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.edu.ifal.fiscalizaapp.data.db.entities.FaqEntity

@Dao
interface FaqDao {
    @Query("SELECT * FROM faq")
    suspend fun getAll(): List<FaqEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(faqs: List<FaqEntity>)

    @Query("DELETE FROM faq")
    suspend fun deleteAll()
}