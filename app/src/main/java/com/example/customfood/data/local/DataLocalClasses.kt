package com.example.customfood.data.local

import com.example.customfood.data.remote.dto.DataItem

data class DataLocalOption(
    val image_name: String,
    val items: List<DataItem>?,
    val name: String,
)
