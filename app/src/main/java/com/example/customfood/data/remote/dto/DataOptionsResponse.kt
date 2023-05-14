package com.example.customfood.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class old_DataOptionsResponse(
    val name: String,
    val items: List<DataItem>,
    val image_name: String,
    val encoded_image: String
) : java.io.Serializable