package com.example.customfood.data.remote.dto

import android.os.Parcelable

@kotlinx.serialization.Serializable
data class DataResponse(
    val item: String,
    val id: Int
)
