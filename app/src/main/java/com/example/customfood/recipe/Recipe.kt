package com.example.customfood.ui

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.customfood.AdapterFoodType
import com.example.customfood.AdapterRecipe
import com.example.customfood.R
import com.example.customfood.data.remote.dto.DataRecipe
import com.example.customfood.data.remote.dto.IRestAPIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Recipe : AppCompatActivity() {
    val TAG = "CustomFood - Recipe"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        Log.d(TAG, "In onCreate: Recipe")

        val whitelist : List<String> = intent.getStringArrayListExtra("EXTRA_INCLUDE_INGREDIENTS") as List<String>
        val blacklist : List<String> = intent.getStringArrayListExtra("EXTRA_IGNORE_INGREDIENTS") as List<String>
        val user_id : String? = intent.getStringExtra("EXTRA_USER_ID")
        val original_recipe: Boolean = intent.getBooleanExtra("EXTRA_ORIGINAL_RECIPE", true)
        val random : Boolean = intent.getBooleanExtra("EXTRA_RANDOM", false)

        if (user_id != null) {
            getRecipe(this, whitelist, blacklist, user_id, original_recipe, random)
        } else {
            Log.e(TAG, "User ID is None")
        }

    }

    fun getRecipe(context: Context, whitelist: List<String>, blacklist: List<String>, userId: String, original_recipe: Boolean, random: Boolean) {

        // Start the long-running task in a coroutine
        GlobalScope.launch(Dispatchers.Main) {
            val result: DataRecipe =
                IRestAPIService.create().getRecipe(whitelist, blacklist, userId, original_recipe, random)

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