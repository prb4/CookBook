package com.example.customfood

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CheckBox : AppCompatActivity() {
    val TAG = "CustomFoodTAG"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO - remove bar at top of screen
        Log.d(TAG, "In CheckBox activity")
        setContentView(R.layout.activity_check_box)

        val foodList = intent.getSerializableExtra("EXTRA_FOODLIST") as ArrayList<Food>
        Log.d(TAG, foodList.toString())
        /*
        var foodList = mutableListOf(
            Food("Chicken", false),
            Food("Trout", false),
            Food("Lamb", false),
            Food("Salmon", false),
            Food("Steak", false)
        )
        */
        //val adapter = FoodAdapter(foodList)
        val rvFood = findViewById<RecyclerView>(R.id.rv_food)
        val submit = findViewById<Button>(R.id.button_submit)

        //rvFood.adapter = adapter
        rvFood.adapter = FoodAdapter(foodList)
        rvFood.layoutManager = LinearLayoutManager(this)

        submit.setOnClickListener{
            Log.d(TAG, "Submit Button Clicked")
            val adapter = rvFood.adapter as FoodAdapter
            val checkedItems = adapter.getCheckedItems()
            Log.d(TAG, "Checked items: $checkedItems")
            val resultIntent = Intent()
            val selectedItems = SelectedItems(checkedItems)
            resultIntent.putExtra("EXTRA_CHECKED_FOODS", selectedItems)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()

        }
    }
}