package com.example.customfood

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.customfood.data.remote.dto.DataFoodChoiceResponse
import com.example.customfood.data.remote.dto.DataRecipeResponse
import com.example.customfood.data.remote.dto.IRecipeService

class MainActivity : ComponentActivity(), IFoodTypeItemClickListener {
    val TAG = "CustomFood - MainActivity"
    val FOOD_TYPE : Int = 1
    private val service = IRecipeService.create()
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Log.d(TAG, "Starting MainActivity")
            //TODO - change to AndroidX / callback variant

            //Start recycler view stuff
            setContentView(R.layout.activity_main)

            val rvOptions = findViewById<RecyclerView>(R.id.rv_options)

            val dataFoodTypes = mutableListOf<DataFoodType>(
                DataFoodType("Main", R.drawable.chicken),
                DataFoodType("Sides", R.drawable.rice),
                DataFoodType("Dessert", R.drawable.dessert)
                )

            rvOptions.adapter = AdapterFoodType(dataFoodTypes, this)
            rvOptions.layoutManager = LinearLayoutManager(this)

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


    override fun onFoodTypeItemClick(foodType: DataFoodType, data: List<DataFoodChoiceResponse>){
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

