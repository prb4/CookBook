package com.example.customfood.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DataOptionsResponse(
    val name: String,
    val items: List<DataItemResponse>,
    val image: String?
) : java.io.Serializable