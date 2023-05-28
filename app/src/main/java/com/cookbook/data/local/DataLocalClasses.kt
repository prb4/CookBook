package com.cookbook.data.local

import com.cookbook.data.remote.dto.DataItem

data class DataLocalOption(
    val image_name: String,
    val items: List<DataItem>?,
    val name: String,
)
