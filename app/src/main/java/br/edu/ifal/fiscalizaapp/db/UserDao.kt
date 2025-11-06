package br.edu.ifal.fiscalizaapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.edu.ifal.fiscalizaapp.model.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE apiId = :apiId LIMIT 1")
    suspend fun getUserByApiId(apiId: Int): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email COLLATE NOCASE LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE cpf = :cpf LIMIT 1")
    suspend fun getUserByCpf(cpf: String): UserEntity?
}