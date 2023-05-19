package com.example.customfood.ui

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.example.customfood.R
import com.example.customfood.data.remote.dto.DataRecipe
import com.example.customfood.data.remote.dto.IRestAPIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Recipe : AppCompatActivity() {
    val TAG = "CustomFood - Recipe"

    private lateinit var progressBar: ProgressBar
    private lateinit var ingredients: TextView
    private lateinit var instructions: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        Log.d(TAG, "In onCreate: Recipe")

        progressBar = findViewById(R.id.progressBar)
        ingredients = findViewById(R.id.ingredients)
        instructions = findViewById(R.id.instructions)

        val whitelist : List<String> = intent.getStringArrayListExtra("EXTRA_INCLUDE_INGREDIENTS") as List<String>
        val blacklist : List<String> = intent.getStringArrayListExtra("EXTRA_IGNORE_INGREDIENTS") as List<String>
        val user_id : String? = intent.getStringExtra("EXTRA_USER_ID")
        val original_recipe: Boolean = intent.getBooleanExtra("EXTRA_ORIGINAL_RECIPE", true)

        if (user_id != null) {
            getRecipe(whitelist, blacklist, user_id, original_recipe)
        } else {
            Log.e(TAG, "User ID is None")
        }

    }

    fun getRecipe(whitelist: List<String>, blacklist: List<String>, userId: String, original_recipe: Boolean) {

        progressBar.visibility = View.VISIBLE
        ingredients.visibility = View.GONE
        instructions.visibility = View.GONE

        // Start the long-running task in a coroutine
        GlobalScope.launch(Dispatchers.Main) {
            val result: DataRecipe =
                IRestAPIService.create().getRecipe(whitelist, blacklist, userId, original_recipe)

            // Update the UI with the result
            progressBar.visibility = View.GONE
            ingredients.visibility = View.VISIBLE
            ingredients.text = result.ingredients.toString()
            instructions.text = result.recipe.toString()
        }
    }
}