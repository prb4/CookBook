package com.example.customfood.ui

import android.util.Log
import com.example.customfood.data.remote.dto.DataPostResponse
import com.example.customfood.data.remote.dto.IRecipeService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WebRequests {
    val TAG = "CustomFood - WebRequests"
    private val service = IRecipeService.create()

    fun startRequest(data: String){
        Log.d(TAG, "in startRequest")
        CoroutineScope(Dispatchers.IO).launch {
            val downloadedDataPostResponse = downloadChoices(data)
            Log.d(TAG, "startRequest data: " + downloadedDataPostResponse.toString())

            // Use the coroutine scope to launch a new coroutine on the main thread
            //withContext(Dispatchers.Main) {
                // Update the UI with the result
                //TODO - set data (dont think i want to do this)
            //}
        }
    }

    private suspend fun downloadChoices(foodType: String) : List<DataPostResponse>{
        Log.d(TAG, "in downloadChoices")
        val response = service.getPost()
        Log.d(TAG, response.toString())
        return response
    }
}