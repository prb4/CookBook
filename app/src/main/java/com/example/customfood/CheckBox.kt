package com.example.customfood

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
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

        Log.d(TAG, "Creating adapter")
        rvFood.adapter = AdapterFoodChoice(dataItemList)
        Log.d(TAG, "Creating grid layout")
        //rvFood.layoutManager = LinearLayoutManager(this)
        rvFood.layoutManager = GridLayoutManager(this, 2)
        Log.d(TAG, "Grid layout configured")

        submit.setOnClickListener{
            Log.d(TAG, "Submit Button Clicked")
            val adapter = rvFood.adapter as AdapterFoodChoice

            val checkedItems = adapter.getSelectedItems()
            val ignoreItems = adapter.getIgnoreItems()

            val arrayCheckedItems : ArrayList<String> = ArrayList(checkedItems)
            val arrayIgnoreItems : ArrayList<String> = ArrayList(ignoreItems)

            Log.d(TAG, "Checked items: ${arrayCheckedItems.size}")

            val resultIntent = Intent()

            resultIntent.putStringArrayListExtra("EXTRA_INGREDIENTS", arrayCheckedItems)
            resultIntent.putStringArrayListExtra("EXTRA_IGNORE_INGREDIENTS", arrayIgnoreItems)
            setResult(Activity.RESULT_OK, resultIntent)

            finish()
        }
    }
}