package br.edu.ifal.fiscalizaapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.edu.ifal.fiscalizaapp.model.ProtocolEntity

@Dao
interface ProtocolDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProtocol(protocol: ProtocolEntity)

    @Query("SELECT * FROM protocols")
    suspend fun getAllProtocols(): List<ProtocolEntity>

}