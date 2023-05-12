package com.example.customfood

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.customfood.data.remote.dto.DataItemResponse
import com.example.customfood.data.remote.dto.DataOptionResponse
import com.example.customfood.data.remote.dto.IRestAPIService
import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext
import kotlin.system.exitProcess

class MainActivity : ComponentActivity(), IFoodTypeItemClickListener {
    val TAG = "CustomFood - MainActivity"
    val FOOD_TYPE : Int = 1
    private val service = IRestAPIService.create()
    var foodOptions = listOf<DataOptionResponse>()
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Log.d(TAG, "Starting MainActivity")
            //TODO - change to AndroidX / callback variant

            //Start recycler view stuff
            setContentView(R.layout.activity_main)

            val rvOptions = findViewById<RecyclerView>(R.id.rv_options)

            val dataFoodTypes = mutableListOf<DataFoodType>()
            runBlocking {
                Log.d(TAG, "Parent: ${kotlin.coroutines.coroutineContext[Job]}")
                foodOptions = downloadOptions()
                for (option in foodOptions) {
                    Log.d(TAG, option.name)
                    //TODO - download image
                    if (!option.image.isNullOrEmpty()) {
                        val job = GlobalScope.launch (Dispatchers.Default) {
                            Log.d(TAG, "Downloading image: " + option.image)
                            val image = downloadImage(option.image)
                            Log.d(TAG, "Image size: " + image.byteCount.toString())
                            dataFoodTypes.add(DataFoodType(option.name, image))
                        }
                        runBlocking {
                            job.join()
                            job.cancel()
                            Log.d(TAG, "Successfully downloaded image for " + option.name)
                        }
                    }
                    Log.d(TAG, "Adding " + option.name + " to dataFoodType")
                }
            }

            Log.d(TAG, "Outside of runBlock")
            rvOptions.adapter = AdapterFoodType(dataFoodTypes, this)
            //rvOptions.layoutManager = LinearLayoutManager(this)
            rvOptions.layoutManager = GridLayoutManager(this, 2)

            // End recycler view stuff
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult")
        if (requestCode == FOOD_TYPE && resultCode == Activity.RESULT_OK && data != null) {
            val returnedData: DataSelectedItems =
                data?.getSerializableExtra("EXTRA_FOOD_CHOICE") as DataSelectedItems

            Log.d(TAG, "Selected items: ${returnedData.toString()}")
            //TODO - make web request
        }
    }

    fun getFoodOptions(foodType: String):  MutableList<DataFoodChoice>{
        Log.d(TAG, "Do we still have the options: " + foodOptions.toString())
        exitProcess(-2)
        var dataFoodChoiceList : MutableList<DataFoodChoice> = arrayListOf()
        if (foodType == "Main") {
            dataFoodChoiceList = mutableListOf(
                DataFoodChoice("Chicken", R.integer.FOOD_TYPE_MAIN, false),
                DataFoodChoice("Trout", R.integer.FOOD_TYPE_MAIN,false),
                DataFoodChoice("Lamb", R.integer.FOOD_TYPE_MAIN,false),
                DataFoodChoice("Salmon", R.integer.FOOD_TYPE_MAIN,false),
                DataFoodChoice("Pork", R.integer.FOOD_TYPE_MAIN,false),
                DataFoodChoice("Steak", R.integer.FOOD_TYPE_MAIN,false)
            )
        }else if (foodType == "Sides") {
            dataFoodChoiceList = mutableListOf(
                DataFoodChoice("Rice", R.integer.FOOD_TYPE_SIDE,false),
                DataFoodChoice("Beans", R.integer.FOOD_TYPE_SIDE,false),
                DataFoodChoice("Asparagus", R.integer.FOOD_TYPE_SIDE,false),
                DataFoodChoice("Broccoli", R.integer.FOOD_TYPE_SIDE,false)
            )
        } else if (foodType == "Dessert") {
            dataFoodChoiceList = mutableListOf(
                DataFoodChoice("Pie", R.integer.FOOD_TYPE_DESSERT,false),
                DataFoodChoice("Donut", R.integer.FOOD_TYPE_DESSERT,false),
                DataFoodChoice("Cheese Cake", R.integer.FOOD_TYPE_DESSERT,false),
                DataFoodChoice("Ice Cream", R.integer.FOOD_TYPE_DESSERT,false)
            )
        }
        Log.d(TAG, "Returning from getFoodOptions with ${dataFoodChoiceList.toString()}")
        return dataFoodChoiceList
    }

    private suspend fun downloadOptions() : List<DataOptionResponse>{
        Log.d(TAG, "in downloadOptions")
        return service.getOptions()
    }

    private suspend fun downloadImage(image: String) : Bitmap{
        Log.d(TAG, "in downloadImage: " + image)
        return IRestAPIService.create().getImage(image)
    }

    override fun onFoodTypeItemClick(foodType: DataFoodType, data: List<DataItemResponse>){
        Log.d(TAG, "Back in MainActivity after clicking on ${foodType.title}")
        Log.d(TAG, data.toString())

        val foodList = getFoodOptions(foodType.title)
        Log.d(TAG, foodList.toString())
        val foodArray = ArrayList(foodList)
        Intent(this,CheckBox::class.java).also {
            it.putExtra("EXTRA_FOODLIST", foodArray)
            startActivityForResult(it, FOOD_TYPE)
        }
    }


}

