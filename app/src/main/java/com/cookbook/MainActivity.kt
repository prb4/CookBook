package com.cookbook

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
import com.cookbook.data.remote.dto.DataItem
import com.cookbook.data.remote.dto.DataOption
import com.cookbook.R
import com.cookbook.ui.Recipe
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.*
import org.json.JSONObject

class MainActivity : ComponentActivity(), IFoodTypeItemClickListener {
    val TAG = "CookBook - MainActivity"
    var foodOptions = JSONObject()
    var ingredients : List<DataItem> = listOf()
    var ignoreIngredients : List<DataItem> = listOf()
    var userId : String = ""
    var original_recipe : Boolean = true

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.Theme_CookBook)
        setContent {
            Log.d(TAG, "Starting MainActivity")
            Firebase.analytics.logEvent("mainActivity", null)
            //TODO - change to AndroidX / callback variant
            //TODO - create onResume (ie: when phone layout changes.  Do we need to make a request each time?

            //Start recycler view stuff

            setContentView(R.layout.activity_main)

            val rvOptions = findViewById<RecyclerView>(R.id.rv_options)
            val submit = findViewById<Button>(R.id.button_submit)
            val dataFoodTypes = mutableListOf<DataFoodType>()
            Log.d(TAG, "Checking options: ${ManageDataSingleton.foodOptions.length().toString()}")
            foodOptions = if (ManageDataSingleton.foodOptions.length() == 0){
                ManageDataSingleton.getOptions()
            } else {
                Log.d(TAG, "Time save - already have foodOptions")
                ManageDataSingleton.foodOptions
            }

            Log.d(TAG, "Have options: ${ManageDataSingleton.foodOptions.length().toString()}")

            runBlocking {
                val keyIterator = foodOptions.keys()
                while(keyIterator.hasNext()){
                    val key = keyIterator.next()
                    val option = Gson().fromJson(foodOptions.get(key).toString(), DataOption::class.java)

                    val image = if (ManageDataSingleton.objectImageDict.has(option.name)){
                        Log.d(TAG, "Time save - no initOptionImage")
                        ManageDataSingleton.objectImageDict.get(option.name) as Bitmap
                    } else {
                        ManageDataSingleton.initOptionImage(option)
                    }

                    dataFoodTypes.add(DataFoodType(option.name, image))
                    Log.d(TAG, "Successfully retrived image for " + option.name)
                }
            }

            GlobalScope.launch(Dispatchers.IO){
                for (key in foodOptions.keys()){
                    //Kick this off so it gets started prior to a selection
                    val data : DataOption = Gson().fromJson(foodOptions.get(key).toString(), DataOption::class.java)
                    if (!data.items?.isEmpty()!!) {
                        for (item in data.items) {
                            if (!ManageDataSingleton.objectImageDict.has(item.name)) {
                                Log.d(TAG, "Initializing ${data.name}")
                                ManageDataSingleton.initializeItem(data, item)
                                //manageData.initializeItem(data, item)
                            } else {
                                Log.d(TAG, "Time save - Not re-initializing the item: ${item.name}")
                            }
                        }
                    }
                }
            }.cancel()

            rvOptions.adapter = AdapterFoodType(dataFoodTypes, this)
            //rvOptions.layoutManager = LinearLayoutManager(this)
            rvOptions.layoutManager = GridLayoutManager(this, 2)

            // End recycler view stuff

            submit.setOnClickListener{
                Log.d(TAG, "Submit Button Clicked in OnCreate")
                val whitelist = mutableListOf<String>()
                val blacklist = mutableListOf<String>()
                for (ingredient in ingredients){
                    whitelist.add(ingredient.name)
                }
                for (ignoreIngredient in ignoreIngredients){
                    blacklist.add(ignoreIngredient.name)
                }
                Log.d(TAG, "Getting recipe for: whitelist: ${whitelist.toString()}, and blacklist: ${blacklist.toString()}")

                Intent(this, Recipe::class.java).also{
                    Log.d(TAG, "Starting activity: Recipe")
                    it.putStringArrayListExtra("EXTRA_INCLUDE_INGREDIENTS", ArrayList(whitelist))
                    it.putStringArrayListExtra("EXTRA_IGNORE_INGREDIENTS", ArrayList(blacklist))
                    it.putExtra("EXTRA_USER_ID", userId)
                    it.putExtra("EXTRA_ORIGINAL_RECIPE", original_recipe)
                    it.putExtra("EXTRA_RANDOM", false)
                    startActivity(it)
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
            val dataOption = data.getSerializableExtra("EXTRA_OPTION") as DataOption

            Log.d(TAG, "Selected items: ${selectedIngredients.toString()}")
            Log.d(TAG, "Avoid items: ${avoidIngredients.toString()}")

            ingredients = ingredients + selectedIngredients!!.toList()
            ignoreIngredients = ignoreIngredients + avoidIngredients!!.toList()

            Log.d(TAG, "All selected items: ${ingredients.toString()}")
            Log.d(TAG, "All ignored items: ${ignoreIngredients.toString()}")

            //val manageData = ManageData()
            ManageDataSingleton.setItems(dataOption, ingredients)
            ManageDataSingleton.setItems(dataOption, ignoreIngredients)
            //manageData.setItems(dataOption, ingredients)
            //manageData.setItems(dataOption, ignoreIngredients)
            //TODO - make web request
            //TODO - save list
        }
    }
    override fun onFoodTypeItemClick(foodOption: String) {
        /*
        Pass the items associated with <foodOption> to the CheckBox class, after clearing out 'encoded_image
         */
        Log.d(TAG, "Back in MainActivity after clicking on ${foodOption}")

        if (foodOption.equals("Random")){
            Log.d(TAG, "Going straight to server")

            Intent(this, Recipe::class.java).also{
                Log.d(TAG, "Starting activity: Recipe")
                it.putStringArrayListExtra("EXTRA_INCLUDE_INGREDIENTS", ArrayList())
                it.putStringArrayListExtra("EXTRA_IGNORE_INGREDIENTS", ArrayList())
                it.putExtra("EXTRA_USER_ID", userId)
                it.putExtra("EXTRA_ORIGINAL_RECIPE", false)
                it.putExtra("EXTRA_RANDOM", true)
                startActivity(it)
            }

        } else {

            val option =
                Gson().fromJson(foodOptions.get(foodOption).toString(), DataOption::class.java)
            if (!option.items?.isEmpty()!!) {
                for (item in option.items) {
                    item.encoded_image = ""
                }
            }
            Intent(this, CheckBox::class.java).also {
                it.putExtra("EXTRA_OPTION", option as java.io.Serializable)
                Log.d(TAG, "Starting activity: Checkbox")
                startActivityForResult(it, R.integer.FOOD_TYPE)
            }
        }
    }
}

