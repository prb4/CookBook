package com.example.customfood.ui

import android.graphics.Bitmap
import android.util.Log
import com.example.customfood.data.remote.dto.*
import com.google.gson.Gson
import io.ktor.http.*
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject

class ManageData {
    val TAG = "CustomFood - ManageData"

    companion object {
        var foodOptions : JSONObject = JSONObject()
        var objectImageDict = JSONObject()
        var foodItemsJson = JSONObject()
    }

    fun getItems(option: DataOption): List<DataItem> {
        Log.d(TAG, "in getItems: ${option.name}")
        return foodItemsJson.get(option.name) as List<DataItem>
    }

    fun setItem(option: DataOption, item: DataItem){
        Log.d(TAG, "in setItem: ${item.name}")
        if (0 == foodItemsJson.length()){
            foodItemsJson.put(option.name, listOf(item))
        } else {
            Log.d(TAG, "Checking foodItemsJson for ${option.name}")

            var foodItems: List<DataItem> = mutableListOf<DataItem>()
            try {
                foodItems = foodItemsJson.get(option.name) as List<DataItem>
            } catch (e: JSONException) {
                Log.e(TAG, "foodItems['${option.name}'] was empty")
                foodItemsJson.put(option.name, listOf(item))
                return
            }

            for (foodItem in foodItems){
                if (foodItem.name.equals(item.name)){
                    //item exist in foodItemJson
                    foodItem.isChecked = item.isChecked
                    Log.d(TAG, "Updating to ${foodItems.toString()}")
                    return
                }
            }
            //item did not previously exist in foodItemJson, so add it
            foodItems = foodItems + item
            Log.d(TAG, "Updating to ${foodItems.toString()}")
            //Store the changes
            foodItemsJson.put(option.name, foodItems)
        }
    }

    fun setItems(option: DataOption, items: List<DataItem>){
        Log.d(TAG, "in setItems")
        if (0 == foodItemsJson.length()){
            foodItemsJson.put(option.name, items)
        } else {
            val foodItems = foodItemsJson.get(option.name) as List<DataItem>
            for (item in items) {
                for (foodItem in foodItems) {
                    if (foodItem.name.equals(item.name)) {
                        foodItem.isChecked = item.isChecked
                        Log.d(TAG, "Changed ${foodItem.name} to ${foodItem.isChecked}")
                    }
                }
            }
        }
    }
    fun initializeItems(option: DataOption){
        /*
        Take in a DataOption, and make sure all of its items are good to go
         */
        Log.d(TAG, "in initializeItems: ${option.name}")

        val util = Util()
        for (item in option.items){
            Log.d(TAG, "initializing ${item.name}")
            if (!(objectImageDict.has(item.name))){
                Log.d(TAG, "Fixing up image for ${item.name}")
                objectImageDict.put(item.name, util.base64ToBitmap(item.encoded_image))
            }
            item.encoded_image = ""
            setItem(option, item)
        }
        Log.d(TAG, "Initialized items: ${foodItemsJson.toString()}")
        Log.d(TAG, objectImageDict.names().toString())
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