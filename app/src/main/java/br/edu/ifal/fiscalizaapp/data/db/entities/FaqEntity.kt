package br.edu.ifal.fiscalizaapp.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "faq")
data class FaqEntity(
    @PrimaryKey
    val id: Int,
    val question: String,
    val answer: String
)