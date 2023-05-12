package com.example.customfood

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.customfood.data.remote.dto.DataItemResponse

class CheckBox (): AppCompatActivity() {
    val TAG = "CustomFood - CheckBox"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO - remove bar at top of screen
        Log.d(TAG, "In CheckBox activity")
        setContentView(R.layout.activity_check_box)

        val dataItemList = intent.getSerializableExtra("EXTRA_FOODLIST") as List<DataItemResponse>
        Log.d(TAG, dataItemList.toString())

        val rvFood = findViewById<RecyclerView>(R.id.rv_food)
        val submit = findViewById<Button>(R.id.button_submit)

        rvFood.adapter = AdapterFoodChoice(dataItemList)
        rvFood.layoutManager = LinearLayoutManager(this)

        submit.setOnClickListener{
            Log.d(TAG, "Submit Button Clicked")
            val adapter = rvFood.adapter as AdapterFoodChoice
            val checkedItems = adapter.getCheckedItems()
            Log.d(TAG, "Checked items: $checkedItems")

            val resultIntent = Intent()
            val dataSelectedItems = DataSelectedItems(checkedItems)
            resultIntent.putExtra("EXTRA_FOOD_CHOICE", dataSelectedItems)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()

        }
    }
}