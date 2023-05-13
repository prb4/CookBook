package com.example.customfood

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.customfood.data.remote.dto.DataItemResponse
import com.example.customfood.data.remote.dto.IRestAPIService
import kotlinx.coroutines.*

class AdapterFoodChoice(
    val dataItemResponses: List<DataItemResponse>
) : RecyclerView.Adapter<AdapterFoodChoice.FoodViewHolder>() {

    val TAG = "CustomFood - AdapterFoodChoice.kt"

    inner class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        //val checkBox : CheckBox = itemView.findViewById<CheckBox>(R.id.cv_selected)
        val description : TextView = itemView.findViewById<CheckBox>(R.id.tv_description)
        val imageView : ImageView = itemView.findViewById<ImageView>(R.id.iv_img)
        val cardView : CardView = itemView.findViewById<CardView>(R.id.cv)
        fun bind(item: DataItemResponse) {
            val context : Context = itemView.context
            Log.d(TAG, "in FoodViewHolder.bind")
            //var image = Bitmap.createBitmap(0, 0, Bitmap.Config.ARGB_8888)
            //val dataFoodType = mutableListOf<DataFoodType>()
            description.text = item.name

            //TODO - pull this out of the function so its not called multiple times
            val job = GlobalScope.launch (Dispatchers.Default) {
                val image = async {
                    Log.d(TAG, "Downloading image: " + item.image)
                    downloadImage(item.image)
                    //dataFoodType.add(DataFoodType(item.name, image))
                }.await()
                imageView.setImageBitmap(image)
                Log.d(TAG, "${item.name} is checked: ${item.isChecked}")
                cardView.setOnClickListener {
                    if (item.isChecked.equals("blank")) {
                        item.isChecked = "selected"
                        cardView.setCardBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.light_green
                            )
                        )
                    } else if (item.isChecked.equals("selected")) {
                        item.isChecked = "ignore"
                        cardView.setCardBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.light_red
                            )
                        )
                    } else if (item.isChecked.equals("ignore")) {
                        item.isChecked = "blank"
                        cardView.setCardBackgroundColor(
                            Color.TRANSPARENT
                        )
                    }
                    Log.d(TAG, "${item.name} was checked to ${item.isChecked}")
                }
                Log.d(TAG, "${item.name} is NOW checked: ${item.isChecked}")
            }
            runBlocking {
                job.join()
                job.cancel()
                Log.d(TAG, "Successfully downloaded image for " + item.name)
            }
        }

        private suspend fun downloadImage(image: String) : Bitmap {
            Log.d(TAG, "in downloadImage: " + image)
            return IRestAPIService.create().getImage(image)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        Log.d(TAG, "in onCreateViewHolder")
        //val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        Log.d(TAG, "in onBindViewHolder")
        //holder.itemView.findViewById<CheckBox>(R.id.cv_selected).buttonTintList = ContextCompat.getColorStateList(holder.itemView.context, R.color.purple_200)
        /*
        val item = foods[position]
        holder.itemView.findViewById<TextView>(R.id.tv_title).text = item.title
        holder.itemView.findViewById<CheckBox>(R.id.cv_selected).isChecked = item.isChecked

        //val item = foods[position]
        //holder.itemView.isSelected = item.isChecked

        holder.itemView.setOnClickListener{
            item.isChecked = !item.isChecked
            holder.itemView.isSelected = item.isChecked
            Log.d(TAG, "Clicked: ${item.title}, status: ${item.isChecked}")
        }
        */
        //Log.d(TAG, "${foods[position].title} is checked: ${foods[position].isChecked}}")
        holder.bind(dataItemResponses[position])
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "in getItemCount")
        return dataItemResponses.size
    }

    fun getSelectedItems(): List<String> {
        Log.d(TAG, "in getCheckedItems")
        return dataItemResponses.filter { it.isChecked.equals("selected") }.map {it.name}
    }

    fun getIgnoreItems(): List<String> {
        Log.d(TAG, "in getCheckedItems")
        return dataItemResponses.filter { it.isChecked.equals("ignore") }.map {it.name}
    }

}