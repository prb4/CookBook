package com.example.customfood

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.customfood.data.remote.dto.DataOption

class CheckBox (): AppCompatActivity() {
    val TAG = "CustomFood - CheckBox"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO - remove bar at top of screen
        Log.d(TAG, "In CheckBox activity")
        setTheme(R.style.Theme_CustomFood)
        setContentView(R.layout.activity_check_box)

        val dataOption = intent.getSerializableExtra("EXTRA_OPTION") as DataOption
        val dataItems = ManageDataSingleton.getItems(dataOption)
        Log.d(TAG, dataItems.toString())

        val rvFood = findViewById<RecyclerView>(R.id.rv_food)
        val submit = findViewById<Button>(R.id.button_submit)

        Log.d(TAG, "Creating adapter")
        rvFood.adapter = AdapterFoodChoice(dataItems)
        //rvFood.layoutManager = LinearLayoutManager(this)
        rvFood.layoutManager = GridLayoutManager(this, 2)

        submit.setOnClickListener{
            Log.d(TAG, "Submit Button Clicked")
            val adapter = rvFood.adapter as AdapterFoodChoice

            val checkedItems = adapter.getSelectedItems()
            val ignoreItems = adapter.getIgnoreItems()

            //val arrayCheckedItems : ArrayList<String> = ArrayList(checkedItems)
            //val arrayIgnoreItems : ArrayList<String> = ArrayList(ignoreItems)

            //Log.d(TAG, "Checked items: ${arrayCheckedItems.size}")

            val resultIntent = Intent()

            resultIntent.putExtra("EXTRA_INGREDIENTS", ArrayList(checkedItems))
            resultIntent.putExtra("EXTRA_IGNORE_INGREDIENTS", ArrayList(ignoreItems))
            resultIntent.putExtra("EXTRA_OPTION", dataOption)
            //resultIntent.putStringArrayListExtra("EXTRA_INGREDIENTS", arrayCheckedItems)
            //resultIntent.putStringArrayListExtra("EXTRA_IGNORE_INGREDIENTS", arrayIgnoreItems)
            setResult(Activity.RESULT_OK, resultIntent)

            finish()
        }
    }
}