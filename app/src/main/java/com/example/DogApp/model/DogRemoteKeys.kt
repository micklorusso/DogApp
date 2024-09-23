package com.example.DogApp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.DogApp.util.Constants.DOG_REMOTE_KEYS_TABLE

@Entity(tableName = DOG_REMOTE_KEYS_TABLE)
data class DogRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val prevPage: Int?,
    val nextPage: Int?
)