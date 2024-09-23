package com.example.DogApp.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.DogApp.util.Constants.DOG_TABLE
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = DOG_TABLE)
data class Dog(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    @SerialName("bred_for")
    val bredFor: String?,
    @Embedded
    val image: DogImage?
)


