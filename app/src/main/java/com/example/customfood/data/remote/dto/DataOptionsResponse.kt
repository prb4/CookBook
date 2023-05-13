package com.example.customfood.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DataOptionsResponse(
    val name: String,
    val items: List<DataItemResponse>,
    val image_name: String,
    val encoded_image: String
) : java.io.Serializable