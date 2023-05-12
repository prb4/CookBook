package com.example.customfood.ui

import android.util.Log
import com.example.customfood.data.remote.dto.DataOptionsResponse
import com.example.customfood.data.remote.dto.IRestAPIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class FoodOptions {
    val TAG = "CustomFood - FoodOptions.kt"
    var foodOptions = listOf<DataOptionsResponse>()

    fun getFoodOptions(test: String): List<DataOptionsResponse> {
        Log.d(TAG, "in getFoodOptions")
        if (foodOptions.isEmpty()) {
            runBlocking {
                Log.d(TAG, "No food options yet, downloading...")
                val job = GlobalScope.launch(Dispatchers.Default) {
                    foodOptions = downloadFoodOptions()
                    Log.d(TAG, "Downloaded foodOptions: " + foodOptions.size)
                }
                job.join()
                job.cancel()
            }

        }
        Log.d(TAG, "Returning: " + foodOptions.toString())
        return foodOptions
    }
    private suspend fun downloadFoodOptions() : List<DataOptionsResponse>{
            Log.d(TAG, "in downloadFoodOptions")
            return IRestAPIService.create().getOptions()
    }
}