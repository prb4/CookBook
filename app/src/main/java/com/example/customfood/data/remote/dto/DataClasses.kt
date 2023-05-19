package com.example.customfood.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DataOptionsResponse(
    val Dessert: DataOption,
    val Main: DataOption,
    val Sides: DataOption
) : java.io.Serializable {
    constructor() : this(DataOption(), DataOption(), DataOption())
}

@Serializable
data class DataOption(
    val image_name: String,
    val items: List<DataItem>,
    val name: String,
    val encoded_image: String
) : java.io.Serializable {
    constructor() : this("", mutableListOf<DataItem>(), "", "")
}

@Serializable
data class DataItem(
    val image_name: String,
    var isChecked: String,
    val name: String,
    var encoded_image: String
) : java.io.Serializable

@Serializable
data class DataRecipe(
    val recipe: List<String>,
    val ingredients: List<String>,
    val recipe_id: String
) : java.io.Serializable