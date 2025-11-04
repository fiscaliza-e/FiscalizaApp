package br.edu.ifal.fiscalizaapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val cpf: String,
    val address: String,
    val profileImage: String? = null
) {
    fun getMaskedCpf(): String {
        if (cpf.length < 11) return cpf
        return "${cpf.take(3)}.${cpf.substring(3, 6)}****-${cpf.takeLast(2)}"
    }
}

