package br.edu.ifal.fiscalizaapp.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(value = ["apiId"], unique = true)]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val apiId: Int?,
    val name: String,
    val cpf: String,
    val email: String,
    val password: String,
)