package br.edu.ifal.fiscalizaapp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.edu.ifal.fiscalizaapp.db.entities.ProtocolEntity

@Dao
interface ProtocolDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertProtocol(protocol: ProtocolEntity)

    @Query("SELECT * FROM protocols")
    suspend fun getAllProtocols(): List<ProtocolEntity>

}