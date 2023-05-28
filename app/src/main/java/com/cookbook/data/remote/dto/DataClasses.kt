package com.cookbook.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DataOptionsResponse(
    val Main: DataOption,
    val Sides: DataOption,
    val Dessert: DataOption,
    val Cultural: DataOption,
    val Random: DataOption
) : java.io.Serializable {
    constructor() : this(DataOption(), DataOption(), DataOption(), DataOption(), DataOption())
}

@Serializable
data class DataOption(
    val image_name: String,
    val items: List<DataItem>?,
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
    val id: String
) : java.io.Serializable

@Serializable
data class DataSaveRecipe(
    val success: Boolean,
) : java.io.Serializable