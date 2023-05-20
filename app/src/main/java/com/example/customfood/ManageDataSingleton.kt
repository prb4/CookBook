package com.example.customfood

import android.graphics.Bitmap
import android.util.Log
import com.example.customfood.data.remote.dto.DataItem
import com.example.customfood.data.remote.dto.DataOption
import com.example.customfood.data.remote.dto.DataOptionsResponse
import com.example.customfood.data.remote.dto.IRestAPIService
import com.example.customfood.ui.Util
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONException
import org.json.JSONObject

object ManageDataSingleton {
    val TAG = "CustomFood - ManageData"

    var foodOptions: JSONObject = JSONObject()
    var objectImageDict = JSONObject()
    var foodItemsJson = JSONObject()


    fun getItems(option: DataOption): List<DataItem> {
        Log.d(TAG, "in getItems: ${option.name}")
        return foodItemsJson.get(option.name) as List<DataItem>
    }

    fun setItem(option: DataOption, item: DataItem) {
        Log.d(TAG, "in setItem: ${item.name}")

        var foodItems: List<DataItem> = mutableListOf<DataItem>()
        if (! foodItemsJson.has(option.name)) {
            //The option key does not exist, add what we have
            Log.e(TAG, "foodItems['${option.name}'] was empty")
            foodItemsJson.put(option.name, listOf(item))
            return
        } else {
            foodItems = foodItemsJson.get(option.name) as List<DataItem>

            for (foodItem in foodItems) {
                if (foodItem.name.equals(item.name)) {
                    //item exist in foodItemJson, update it
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
        for (item in items){
            setItem(option, item)
        }
    }

    fun initializeItem(option: DataOption, item: DataItem) {
        /*
            Take in a DataItem, and make its image is downloaded and in objectImageDict
        */
        Log.d(TAG, "in initializeItems: ${item.name}")

        Log.d(TAG, "initializing ${item.name}")
        if (!(objectImageDict.has(item.name))) {
            Log.d(TAG, "Fixing up image for ${item.name}")
            initItemImage(item)

            item.encoded_image = ""
            setItem(option, item)

        }
        Log.d(TAG, "Initialized item ${foodItemsJson.toString()}")
    }

    fun getOptions(): JSONObject {
    /*
        Download all the options. Should only need to be executed once, as the options shouldn't update on server side at any point thru out the app
     */
        Log.d(TAG, "in getOptions")
        if (foodOptions.length() == 0) {
            runBlocking {
                Log.d(TAG, "No food options yet, downloading...")
                val job = GlobalScope.launch(Dispatchers.Default) {
                    foodOptions = downloadFoodOptions()
                    Log.d(TAG, "Downloaded foodOptions: " + foodOptions.toString())
                }
                job.join()
                job.cancel()
            }
        } else {
            Log.d(TAG, "Time save - Already have food options")
        }
        Log.d(TAG, "Returning: " + foodOptions.toString())
        return foodOptions
    }

    private suspend fun downloadFoodOptions(): JSONObject {
        Log.d(TAG, "in downloadFoodOptions")
        val gson = Gson()

        val data = IRestAPIService.create().getOptions()
        return JSONObject(gson.toJson(data))
    }

    private suspend fun downloadFoodOption(option: String): DataOptionsResponse {
        Log.d(TAG, "in downloadFoodOption")
        return IRestAPIService.create().getOption(option)
    }

    private suspend fun downloadFoodItems(option: String): List<DataItem> {
        Log.d(TAG, "in downloadFoodItems")
        return IRestAPIService.create().getItems(option)
    }

    private suspend fun downloadFoodItem(item: String): DataItem {
        Log.d(TAG, "in downloadFoodItem")
        return IRestAPIService.create().getItem(item)
    }

    fun initItemImage(item: DataItem): Bitmap {
        val util = Util()
        val image = util.base64ToBitmap(item.encoded_image)
        objectImageDict.put(item.name, image as Bitmap)
        return image
    }

    fun initOptionImage(option: DataOption): Bitmap {
        val util = Util()
        val image = util.base64ToBitmap(option.encoded_image)
        objectImageDict.put(option.name, image as Bitmap)
        return image

    }


}