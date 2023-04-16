package com.example.customfood.data.remote.dto

@kotlinx.serialization.Serializable
data class DataPostResponse(
    val body: String,
    val title: String,
    val id: Int,
    val userId: Int
)
