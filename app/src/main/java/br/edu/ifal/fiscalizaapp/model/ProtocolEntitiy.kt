package br.edu.ifal.fiscalizaapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "protocols")
data class ProtocolEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val category: String,
    val description: String,
    val useMyLocation: Boolean,
    val cep: String,
    val rua: String,
    val bairro: String,
    val numero: String,
    val pontoReferencia: String,
    val status: String,
    val date: String,
    val userId: Int
)