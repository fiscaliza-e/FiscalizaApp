package br.edu.ifal.fiscalizaapp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.edu.ifal.fiscalizaapp.db.entities.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getUser(): UserEntity?

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAll(users: List<UserEntity>)

    @Query("SELECT * FROM users WHERE apiId = :apiId LIMIT 1")
    suspend fun getUserByApiId(apiId: Int): UserEntity?

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE email = :email COLLATE NOCASE LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: Long)

    @Query("SELECT COUNT(*) FROM users")
    suspend fun getUserCount(): Int

    @Query("SELECT * FROM users WHERE cpf = :cpf LIMIT 1")
    suspend fun getUserByCpf(cpf: String): UserEntity?
}