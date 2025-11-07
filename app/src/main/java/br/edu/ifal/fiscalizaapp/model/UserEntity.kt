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
    val address: String,
    val profileImage: String? = null,
    val email: String,
    val password: String,
) {
    fun getMaskedCpf(): String {
        if (cpf.length < 11) return cpf
        return "${cpf.take(3)}.${cpf.substring(3, 6)}****-${cpf.takeLast(2)}"
    }
}
