package com.example.DogApp.model

import kotlinx.serialization.Serializable

@Serializable
data class DogImage(
    val url: String
)