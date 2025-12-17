package br.edu.ifal.fiscalizaapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.edu.ifal.fiscalizaapp.data.db.entities.ProtocolEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProtocolDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(protocols: List<ProtocolEntity>)

    @Query("SELECT * FROM protocols WHERE userId = :userId")
    fun getProtocolsByUserId(userId: Int): Flow<List<ProtocolEntity>>

    @Query("DELETE FROM protocols WHERE userId = :userId")
    suspend fun deleteByUserId(userId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(protocol: ProtocolEntity)

    @Query("SELECT * FROM protocols")
    suspend fun getAll(): List<ProtocolEntity>

    @Query("DELETE FROM protocols WHERE userId = :userId")
    suspend fun deleteProtocolsByUserId(userId: Int)
}
