package com.example.customfood.ui

import android.graphics.Bitmap
import android.util.Log
import com.example.customfood.data.remote.dto.*
import com.google.gson.Gson
import io.ktor.http.*
import kotlinx.coroutines.*
import org.json.JSONObject

class ManageData {
    val TAG = "CustomFood - ManageData"

    companion object {
        var foodOptions : JSONObject = JSONObject()
        var objectImageDict = JSONObject()
    }
    fun getItems(option: DataOption){
        /*
        Take in a DataOption, and make sure all of its items are good to go
         */
        Log.d(TAG, "in getItems: ${option.name}")

        val util = Util()
        for (item in option.items){
            if (!(objectImageDict.has(item.name))){
                objectImageDict.put(item.name, util.base64ToBitmap(item.encoded_image))
            }
        }
        Log.d(TAG, objectImageDict.names().toString())
    }

    fun getItem(item: DataItem) : DataItem{
        var foodItem : DataItem = DataItem("","","", "")
        Log.d(TAG, "in getItem)")
        val job = GlobalScope.launch(Dispatchers.Default) {
            foodItem = downloadFoodItem(item.name)
    }

    Log.d(TAG, "Returning: " + foodItem.toString())
    return foodItem
    }

    fun getOptions(): JSONObject{
        Log.d(TAG, "in getOptions")
        runBlocking {
            Log.d(TAG, "No food options yet, downloading...")
            val job = GlobalScope.launch(Dispatchers.Default) {
                foodOptions = downloadFoodOptions()
                Log.d(TAG, "Downloaded foodOptions: " + foodOptions.toString())
            }
            job.join()
            job.cancel()
        }
        Log.d(TAG, "Returning: " + foodOptions.toString())
        return foodOptions
    }
    /*
    fun getFoodItem(item: String): DataItemResponse {
        Log.d(TAG, "in getFoodItems")
        var foodItem : DataItemResponse
        //TODO
        //Check if list exists
        //If so, is the item in there
        //If so, return the item
        //Else, download the item

        runBlocking {
            Log.d(TAG, "No food options yet, downloading...")
            val job = GlobalScope.launch(Dispatchers.Default) {
                foodItem = downloadFoodItem(item)
                Log.d(TAG, "Downloaded foodOptions: " + foodOptions.size)
            }
            job.join()
            job.cancel()
        }

        Log.d(TAG, "Returning: " + foodItems.toString())
        return foodItem
    }

     */

    private suspend fun downloadFoodOptions() : JSONObject{
        Log.d(TAG, "in downloadFoodOptions")
        val gson = Gson()

        val data = IRestAPIService.create().getOptions()
        return JSONObject(gson.toJson(data))
    }

    private suspend fun downloadFoodOption(option: String) : DataOptionsResponse{
        Log.d(TAG, "in downloadFoodOption")
        return IRestAPIService.create().getOption(option)
    }

    private suspend fun downloadFoodItems(option: String) : List<DataItem>{
        Log.d(TAG, "in downloadFoodItems")
        return IRestAPIService.create().getItems(option)
    }

    private suspend fun downloadFoodItem(item: String) : DataItem{
        Log.d(TAG, "in downloadFoodItem")
        return IRestAPIService.create().getItem(item)
    }

    fun getOptionImage(option: DataOption) : Bitmap {
        val util = Util()
        return util.base64ToBitmap(option.encoded_image)
    }
}