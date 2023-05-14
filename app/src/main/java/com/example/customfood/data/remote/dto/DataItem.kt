package com.example.customfood.data.remote.dto

@kotlinx.serialization.Serializable
data class old_DataItem(
    val name : String,
    val image_name : String,
    var encoded_image: String,
    var isChecked : String //Implementing as a string due to API restraints
) : java.io.Serializable