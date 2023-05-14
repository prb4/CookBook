package com.example.customfood.ui

import android.graphics.Bitmap
import android.util.Log
import com.example.customfood.data.remote.dto.DataItemResponse
import com.example.customfood.data.remote.dto.DataOptionsResponse
import com.example.customfood.data.remote.dto.IRestAPIService
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.JsonObject
import org.json.JSONObject

class ManageData {
    val TAG = "CustomFood - ManageData"

    companion object {
        var foodOptions = listOf<DataOptionsResponse>()
        var objectImageDict = JSONObject()
    }
    fun getItems(option: DataOptionsResponse){
        Log.d(TAG, "in getItems: ${option}")
        for (foodOption in foodOptions){
            if (foodOption.name.equals(option.name)){
                val util = Util()
                for (item in foodOption.items){
                    if (!(objectImageDict.has(item.name))){
                        objectImageDict.put(item.name, util.base64ToBitmap(item.encoded_image))
                    }
                }
            }
        }
        Log.d(TAG, objectImageDict.names().toString())
    }

    fun getItem(item: DataItemResponse) : DataItemResponse{
        var foodItem : DataItemResponse = DataItemResponse("","","", "")
        Log.d(TAG, "in getItem)")
        val job = GlobalScope.launch(Dispatchers.Default) {
            foodItem = downloadFoodItem(item.name)
    }

    Log.d(TAG, "Returning: " + foodItem.toString())
    return foodItem
    }
    fun getOption(option: String): DataOptionsResponse{
        Log.d(TAG, "in getFoodOptions")
        if (foodOptions.isEmpty()) {
            getOptions()
        }

        for (foodOption in foodOptions){
            if (foodOption.name.equals(option)){
                return foodOption
            }
        }

        var foodOption : DataOptionsResponse = DataOptionsResponse("", listOf(),"","")
        CoroutineScope(Dispatchers.IO).launch {
            foodOption = downloadFoodOption(option)
            //TODO - this may break
            foodOptions = foodOptions + listOf<DataOptionsResponse>(foodOption)
        }.cancel()

        return foodOption
    }

    fun getOptions(): List<DataOptionsResponse>{
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

    private suspend fun downloadFoodOptions() : List<DataOptionsResponse>{
        Log.d(TAG, "in downloadFoodOptions")
        return IRestAPIService.create().getOptions()
    }

    private suspend fun downloadFoodOption(option: String) : DataOptionsResponse{
        Log.d(TAG, "in downloadFoodOption")
        return IRestAPIService.create().getOption(option)
    }

    private suspend fun downloadFoodItems(option: String) : List<DataItemResponse>{
        Log.d(TAG, "in downloadFoodItems")
        return IRestAPIService.create().getItems(option)
    }

    private suspend fun downloadFoodItem(item: String) : DataItemResponse{
        Log.d(TAG, "in downloadFoodItem")
        return IRestAPIService.create().getItem(item)
    }

    fun getOptionImage(option: DataOptionsResponse) : Bitmap {
        if (foodOptions.isEmpty()) {
            //No foodOptions, get them all
            getOptions()
        }

        if (option in foodOptions){
            //Have the correct option, convert the bitmap and return it
            //#TODO - save the bitmap somewhere
            val util = Util()
            return util.base64ToBitmap(option.encoded_image)
        }else{
            //There are some foodOptions, but not the one we care about here. So download the single option and recursively call the same function
            getOption(option.name)
            return getOptionImage(option)
        }
    }
}