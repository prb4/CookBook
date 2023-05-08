package com.example.customfood.data.remote.dto

@kotlinx.serialization.Serializable
data class DataItemResponse(
    val name : String,
    val image : String?
)
