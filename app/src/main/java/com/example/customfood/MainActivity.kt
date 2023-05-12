package com.example.customfood

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.customfood.data.remote.dto.DataOptionsResponse
import com.example.customfood.data.remote.dto.IRestAPIService
import com.example.customfood.ui.FoodOptions
import kotlinx.coroutines.*

class MainActivity : ComponentActivity(), IFoodTypeItemClickListener {
    val TAG = "CustomFood - MainActivity"
    val FOOD_TYPE : Int = 1
    private val service = IRestAPIService.create()
    var foodOptions = listOf<DataOptionsResponse>()
    //var ingredients = List<DataSelectedItems>
    var ingredients : List<String> = listOf()

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Log.d(TAG, "Starting MainActivity")
            //TODO - change to AndroidX / callback variant

            //Start recycler view stuff
            setContentView(R.layout.activity_main)

            val rvOptions = findViewById<RecyclerView>(R.id.rv_options)
            val submit = findViewById<Button>(R.id.button_submit)

            val dataFoodTypes = mutableListOf<DataFoodType>()
            runBlocking {
                Log.d(TAG, "Parent: ${kotlin.coroutines.coroutineContext[Job]}")
                val foodOptionsClass = FoodOptions()
                foodOptions = foodOptionsClass.getFoodOptions("tset")
                Log.d(TAG, "Options: " + foodOptions.size)
                //foodOptions = downloadOptions()
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

            submit.setOnClickListener{
                Log.d(TAG, "Submit Button Clicked in OnCreate")
                runBlocking {
                    val result = IRestAPIService.create().getRecipe(ingredients)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult")
        if (requestCode == FOOD_TYPE && resultCode == Activity.RESULT_OK && data != null) {
            //val returnedData: DataSelectedItems =
            //    data?.getSerializableExtra("EXTRA_FOOD_CHOICE") as DataSelectedItems
            Log.d(TAG, "Getting choices...")
            val returnedData = data.getStringArrayListExtra("EXTRA_FOOD_CHOICE")

            Log.d(TAG, "Selected items: ${returnedData.toString()}")
            ingredients = ingredients + returnedData!!.toList()
            Log.d(TAG, "All items: ${ingredients.toString()}")
            //TODO - make web request
        }
    }

    private suspend fun downloadImage(image: String) : Bitmap{
        Log.d(TAG, "in downloadImage: " + image)
        return IRestAPIService.create().getImage(image)
    }

    override fun onFoodTypeItemClick(foodOption: String) {
        Log.d(TAG, "Back in MainActivity after clicking on ${foodOption}")

        for (option in foodOptions) {
            if (option.name.equals(foodOption)) {
                Log.d(TAG, "Found ${option.name}")
                val dataItemResponseList = option.items
                Log.d(TAG, "Packaging ${dataItemResponseList.toString()}")
                Intent(this, CheckBox::class.java).also {
                    it.putExtra("EXTRA_FOODLIST", dataItemResponseList as java.io.Serializable)
                    Log.d(TAG, "Starting activity: Checkbox")
                    startActivityForResult(it, FOOD_TYPE)
                }
            }
        }
    }
}

