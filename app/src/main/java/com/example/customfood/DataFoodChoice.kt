package com.example.customfood

data class DataFoodChoice(
    val title: String,
    val foodType: Int,
    var isChecked: Boolean
) : java.io.Serializable