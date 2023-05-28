package com.cookbook.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log

class Util {
    val TAG = "CookBook - Util"

    fun base64ToBitmap(base64String: String): Bitmap {
        Log.d(TAG, "in base64ToBitmap: ${base64String}")
        val imageBytes = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
}