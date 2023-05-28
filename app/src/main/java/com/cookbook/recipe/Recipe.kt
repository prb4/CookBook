package com.cookbook.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cookbook.AdapterRecipe
import com.cookbook.data.remote.dto.DataRecipe
import com.cookbook.data.remote.dto.DataSaveRecipe
import com.cookbook.data.remote.dto.IRestAPIService
import com.cookbook.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Recipe : AppCompatActivity() {
    val TAG = "CookBook - Recipe"
    var recipeId : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setTheme(R.style.Theme_CookBook)
        setContentView(R.layout.activity_recipe)

        Log.d(TAG, "In onCreate: Recipe")

        val whitelist : List<String> = intent.getStringArrayListExtra("EXTRA_INCLUDE_INGREDIENTS") as List<String>
        val blacklist : List<String> = intent.getStringArrayListExtra("EXTRA_IGNORE_INGREDIENTS") as List<String>
        val user_id : String? = intent.getStringExtra("EXTRA_USER_ID")
        val original_recipe: Boolean = intent.getBooleanExtra("EXTRA_ORIGINAL_RECIPE", true)
        val random : Boolean = intent.getBooleanExtra("EXTRA_RANDOM", false)
        //val save_button : Button = findViewById<Switch>(R.id.save_button)

        if (user_id != null) {
            getRecipe(this, whitelist, blacklist, user_id, original_recipe, random)
        } else {
            Log.e(TAG, "User ID is None")
        }

        //TODO - Keep dull'd out unless there is a log in
        /*
        save_button.setOnClickListener{
            Log.d(TAG, "Save button clicked. ${recipeId} saving")
            GlobalScope.launch(Dispatchers.Main) {
                 val result : DataSaveRecipe =
                    IRestAPIService.create()
                        .saveRecipe(user_id, recipeId)

                Log.d(TAG, "Recipe ${recipeId} was successfully saved: ${result.success.toString()}")
            }
        }
         */
    }

    fun getRecipe(context: Context, whitelist: List<String>, blacklist: List<String>, userId: String, original_recipe: Boolean, random: Boolean) {

        // Start the long-running task in a coroutine
        GlobalScope.launch(Dispatchers.Main) {
            val result: DataRecipe =
                IRestAPIService.create().getRecipe(whitelist, blacklist, userId, original_recipe, random)

            recipeId = result.id

            // Update the UI with the result
            val rvInstructions = findViewById<RecyclerView>(R.id.rv_instructions)
            val rvIngredients = findViewById<RecyclerView>(R.id.rv_ingredients)

            rvInstructions.adapter = AdapterRecipe(result.recipe)
            rvInstructions.layoutManager = LinearLayoutManager(context)

            rvIngredients.adapter = AdapterRecipe(result.ingredients)
            rvIngredients.layoutManager = LinearLayoutManager(context)
        }
    }
}