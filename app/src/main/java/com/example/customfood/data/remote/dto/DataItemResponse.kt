package com.example.customfood.data.remote.dto

import android.os.Parcel
import android.os.Parcelable

@kotlinx.serialization.Serializable
data class DataItemResponse(
    val name : String,
    val image_name : String,
    var encoded_image: String,
    var isChecked : String //Implementing as a string due to API restraints
) : java.io.Serializable