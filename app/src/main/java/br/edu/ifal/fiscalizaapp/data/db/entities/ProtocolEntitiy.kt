package br.edu.ifal.fiscalizaapp.data.db.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "protocols",
    indices = [Index(value = ["protocolNumber"], unique = true)]
)
data class ProtocolEntity(
    @PrimaryKey
    val protocolNumber: String,
    val title: String,
    val description: String,
    val status: String,
    val date: String,
    val userId: Int,
    val cep: String,
    val rua: String,
    val bairro: String,
    val numero: String,
    val pontoReferencia: String,
    val useMyLocation: Boolean,
    val photoUris: String = "",
    val numeroPoste: String = "",
    val areaSaneamento: String = "",
    val nomeOrgao: String = "",
    val numeroTransporte: String = "",
    val linhaTransporte: String = "",
    val horarioTransporte: String = ""
)
