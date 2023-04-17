package com.example.customfood.data.remote.dto

@kotlinx.serialization.Serializable
data class DataRecipeResponse(
    val item: String,
    val title: String,
    val id: Int,
    val userId: Int
)
