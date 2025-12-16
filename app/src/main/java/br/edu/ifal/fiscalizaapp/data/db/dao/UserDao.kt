package br.edu.ifal.fiscalizaapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.edu.ifal.fiscalizaapp.data.db.entities.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getUser(): UserEntity?

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAll(users: List<UserEntity>)
    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE apiId = :apiId LIMIT 1")
    suspend fun getByApiId(apiId: Int): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email COLLATE NOCASE LIMIT 1")
    suspend fun getByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE cpf = :cpf LIMIT 1")
    suspend fun getByCpf(cpf: String): UserEntity?

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: Long)

    @Query("SELECT COUNT(*) FROM users")
    suspend fun getCount(): Int
}