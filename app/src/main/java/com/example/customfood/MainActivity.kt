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
import com.example.customfood.ui.ManageData
import com.example.customfood.ui.Util
import kotlinx.coroutines.*

class MainActivity : ComponentActivity(), IFoodTypeItemClickListener {
    val TAG = "CustomFood - MainActivity"
    var foodOptions = listOf<DataOptionsResponse>()
    var ingredients : List<String> = listOf()
    var ignoreIngredients : List<String> = listOf()
    var userId : String = ""
    var original_recipe : Boolean = true

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
            val manageData = ManageData()
            val dataFoodTypes = mutableListOf<DataFoodType>()

            foodOptions = manageData.getOptions()

            runBlocking {
                Log.d(TAG, "Options: " + foodOptions.size)
                for (option in foodOptions) {
                    Log.d(TAG, option.name)

                    val image = manageData.getOptionImage(option)
                    dataFoodTypes.add(DataFoodType(option.name, image))
                    Log.d(TAG, "Successfully retrived image for " + option.name)
                }
            }

            GlobalScope.launch(Dispatchers.IO){
                for (option in foodOptions){
                    //Kick this off so it gets started prior to a selection
                    manageData.getItems(option)
                }
            }.cancel()

            rvOptions.adapter = AdapterFoodType(dataFoodTypes, this)
            //rvOptions.layoutManager = LinearLayoutManager(this)
            rvOptions.layoutManager = GridLayoutManager(this, 2)

            // End recycler view stuff

            submit.setOnClickListener{
                Log.d(TAG, "Submit Button Clicked in OnCreate")
                runBlocking {
                    val result = IRestAPIService.create().getRecipe(ingredients, ignoreIngredients, userId, original_recipe)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult")
        if (requestCode == R.integer.FOOD_TYPE && resultCode == Activity.RESULT_OK && data != null) {
            //val returnedData: DataSelectedItems =
            //    data?.getSerializableExtra("EXTRA_FOOD_CHOICE") as DataSelectedItems
            val selectedIngredients = data.getStringArrayListExtra("EXTRA_INGREDIENTS")
            val avoidIngredients = data.getStringArrayListExtra("EXTRA_IGNORE_INGREDIENTS")

            Log.d(TAG, "Selected items: ${selectedIngredients.toString()}")
            Log.d(TAG, "Avoid items: ${avoidIngredients.toString()}")

            ingredients = ingredients + selectedIngredients!!.toList()
            ignoreIngredients = ignoreIngredients + avoidIngredients!!.toList()

            Log.d(TAG, "All selected items: ${ingredients.toString()}")
            Log.d(TAG, "All ignored items: ${ignoreIngredients.toString()}")
            //TODO - make web request
            //TODO - save list
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
                for (item in dataItemResponseList){
                    item.encoded_image = ""
                }
                Log.d(TAG, "Packaging ${dataItemResponseList.toString()}")
                Intent(this, CheckBox::class.java).also {
                    it.putExtra("EXTRA_FOODLIST", dataItemResponseList as java.io.Serializable)
                    Log.d(TAG, "Starting activity: Checkbox")
                    startActivityForResult(it, R.integer.FOOD_TYPE)
                }
            }
        }
    }
}

