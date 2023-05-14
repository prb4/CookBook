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
import com.example.customfood.data.remote.dto.DataItem
import com.example.customfood.data.remote.dto.DataOption
import com.example.customfood.data.remote.dto.DataOptionsResponse
import com.example.customfood.data.remote.dto.IRestAPIService
import com.example.customfood.ui.ManageData
import com.google.gson.Gson
import kotlinx.coroutines.*
import org.json.JSONObject
import kotlin.reflect.full.memberProperties
import kotlin.system.exitProcess

class MainActivity : ComponentActivity(), IFoodTypeItemClickListener {
    val TAG = "CustomFood - MainActivity"
    var foodOptions = JSONObject()
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
            Log.d(TAG, "Have options")

            runBlocking {
                val keyIterator = foodOptions.keys()
                while(keyIterator.hasNext()){
                    val key = keyIterator.next()
                    val option = Gson().fromJson(foodOptions.get(key).toString(), DataOption::class.java)
                    val image = manageData.getOptionImage(option)
                    dataFoodTypes.add(DataFoodType(option.name, image))
                    Log.d(TAG, "Successfully retrived image for " + option.name)
                }
            }

            GlobalScope.launch(Dispatchers.IO){
                for (key in foodOptions.keys()){
                    //Kick this off so it gets started prior to a selection
                    val data = Gson().fromJson(foodOptions.get(key).toString(), DataOption::class.java)

                    manageData.getItems(data)
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
            //val selectedIngredients = data.getStringArrayListExtra("EXTRA_INGREDIENTS")
            //val avoidIngredients = data.getStringArrayListExtra("EXTRA_IGNORE_INGREDIENTS")
            val selectedIngredients = data.getSerializableExtra("EXTRA_INGREDIENTS") as List<DataItem>
            val avoidIngredients = data.getSerializableExtra("EXTRA_IGNORE_INGREDIENTS") as List<DataItem>

            Log.d(TAG, "Selected items: ${selectedIngredients.toString()}")
            Log.d(TAG, "Avoid items: ${avoidIngredients.toString()}")

            //ingredients = ingredients + selectedIngredients!!.toList()
            //ignoreIngredients = ignoreIngredients + avoidIngredients!!.toList()

            Log.d(TAG, "All selected items: ${ingredients.toString()}")
            Log.d(TAG, "All ignored items: ${ignoreIngredients.toString()}")
            //TODO - make web request
            //TODO - save list
        }
    }
    override fun onFoodTypeItemClick(foodOption: String) {
        /*
        Pass the items associated with <foodOption> to the CheckBox class, after clearing out 'encoded_image
         */
        Log.d(TAG, "Back in MainActivity after clicking on ${foodOption}")

        val option = Gson().fromJson(foodOptions.get(foodOption).toString(), DataOption::class.java)
        for (item in option.items){
            item.encoded_image = ""
        }
        Intent(this, CheckBox::class.java).also {
            it.putExtra("EXTRA_FOODLIST", option.items as java.io.Serializable)
            Log.d(TAG, "Starting activity: Checkbox")
            startActivityForResult(it, R.integer.FOOD_TYPE)
        }
    }
}

