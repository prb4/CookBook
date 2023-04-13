package com.example.customfood

import android.os.Parcelable

data class Food(
    val title: String,
    var isChecked: Boolean
) : java.io.Serializable