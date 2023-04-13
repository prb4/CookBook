package com.example.customfood

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : ComponentActivity(), IFoodOptionsItemClickListener {
    val TAG = "CustomFoodTAG"
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Log.d(TAG, "Starting MainActivity")
            //TODO - change to AndroidX / callback variant

            //Start recycler view stuff
            setContentView(R.layout.activity_main)

            val rvOptions = findViewById<RecyclerView>(R.id.rv_options)

            val foodOptions = mutableListOf<FoodOption>(
                FoodOption("Main", R.drawable.chicken),
                FoodOption("Sides", R.drawable.rice),
                FoodOption("Dessert", R.drawable.dessert)
                )

            rvOptions.adapter = OptionAdapter(foodOptions, this)
            rvOptions.layoutManager = LinearLayoutManager(this)

            // End recycler view stuff

            /*
            val painter = painterResource(id = R.drawable.chicken)
            val description = "My food description"
            val title = "My title"


            Box(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
            ) {
                ImageCard(painter = painter,
                    contentDescription = description,
                    title = title)
            }
             */
            /*
            val foodList = getFoodOptions("MAIN")
            val foodArray = ArrayList(foodList)

            Intent(this,CheckBox::class.java).also {
                it.putExtra("EXTRA_FOODLIST", foodArray)
                startActivityForResult(it, 1)
            }

             */


        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            val returnedData: SelectedItems =
                data?.getSerializableExtra("EXTRA_CHECKED_FOODS") as SelectedItems

            Log.d(TAG, "Selected main items: ${returnedData.toString()}")

            val foodList = getFoodOptions("SIDES")
            val foodArray = ArrayList(foodList)
            Intent(this,CheckBox::class.java).also {
                it.putExtra("EXTRA_FOODLIST", foodArray)
                startActivityForResult(it, 2)
            }

        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            val returnedData: SelectedItems =
                data?.getSerializableExtra("EXTRA_CHECKED_FOODS") as SelectedItems

            Log.d(TAG, "Selected side dish: ${returnedData.toString()}")
        }
    }

    fun getFoodOptions(foodType: String):  MutableList<Food>{
        var foodList : MutableList<Food> = arrayListOf()
        if (foodType == "Main") {
            foodList = mutableListOf(
                Food("Chicken", false),
                Food("Trout", false),
                Food("Lamb", false),
                Food("Salmon", false),
                Food("Pork", false),
                Food("Steak", false)
            )
        }else if (foodType == "Sides") {
            foodList = mutableListOf(
                Food("Rice", false),
                Food("Beans", false),
                Food("Asparagus", false),
                Food("Broccoli", false)
            )
        } else if (foodType == "Dessert") {
            foodList = mutableListOf(
                Food("Pie", false),
                Food("Donut", false),
                Food("Cheese Cake", false),
                Food("Ice Cream", false)
            )
        }
        Log.d(TAG, "Returning from getFoodOptions with ${foodList.toString()}")
        return foodList
    }


    /*
    @Composable
    fun ColorBox(
        modifier: Modifier = Modifier,
        updateColor: (Color) -> Unit
    ) {

        Box(modifier = Modifier
            .background(Color.Red)

            .clickable {
                updateColor(
                    Color(
                        Random.nextFloat(),
                        Random.nextFloat(),
                        Random.nextFloat(),
                        1f
                    )
                )
            }
        )
    }
    */

    /*
    @Composable
    fun Greeting(name: String) {
        Text(text = "Hello $name!")
    }
    */

    /*
    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        CustomFoodTheme {
            Greeting("Android")
        }
    }
    */


    @Composable
    fun ImageCard(
        painter: Painter,
        contentDescription: String,
        title: String,
        modifier: Modifier = Modifier
    ) {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(15.dp),
            elevation = 5.dp
        ) {
            Box(modifier = Modifier.height(200.dp)) {
                androidx.compose.foundation.Image(
                    painter = painter,
                    contentDescription = contentDescription,
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black
                                ),
                                startY = 400f //TODO - make this a dynamic number
                            )
                        )

                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text(
                        title,
                        style = androidx.compose.ui.text.TextStyle(
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    )
                }
            }
        }
    }

    override fun onFoodOptionsItemClick(name: String) {
        Log.d(TAG, "Back in MainActivity after clicking on $name")
        val foodList = getFoodOptions(name)
        Log.d(TAG, foodList.toString())
        val foodArray = ArrayList(foodList)
        Intent(this,CheckBox::class.java).also {
            it.putExtra("EXTRA_FOODLIST", foodArray)
            startActivityForResult(it, 1)
        }
    }
}

