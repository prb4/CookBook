package com.example.customfood

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.customfood.data.remote.dto.DataItemResponse
import com.example.customfood.data.remote.dto.IRestAPIService
import kotlinx.coroutines.*

class AdapterFoodChoice(
    val dataItemResponses: List<DataItemResponse>
) : RecyclerView.Adapter<AdapterFoodChoice.FoodViewHolder>() {

    val TAG = "CustomFood - AdapterFoodChoice.kt"

    inner class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val checkBox : CheckBox = itemView.findViewById<CheckBox>(R.id.cv_selected)
        val imageView : ImageView = itemView.findViewById<ImageView>(R.id.iv_img)
        fun bind(item: DataItemResponse) {
            Log.d(TAG, "in FoodViewHolder.bind")
            //var image = Bitmap.createBitmap(0, 0, Bitmap.Config.ARGB_8888)
            //val dataFoodType = mutableListOf<DataFoodType>()
            checkBox.text = item.name

            //TODO - pull this out of the function so its not called multiple times
            val job = GlobalScope.launch (Dispatchers.Default) {
                val image = async {
                    Log.d(TAG, "Downloading image: " + item.image)
                    downloadImage(item.image)
                    //dataFoodType.add(DataFoodType(item.name, image))
                }.await()
                imageView.setImageBitmap(image)
                Log.d(TAG, "${item.image} is checked: ${item.isChecked}")
                if (item.isChecked.equals("true")){
                    checkBox.isChecked = true
                    item.isChecked.equals("false")
                } else {
                    checkBox.isChecked = false
                    item.isChecked.equals("true")
                }
                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    if (item.isChecked.equals("true")){
                        item.isChecked = "false"
                    } else {
                        item.isChecked = "true"
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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
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

    fun getCheckedItems(): List<String> {
        Log.d(TAG, "in getCheckedItems")
        return dataItemResponses.filter { it.isChecked.equals("true") }.map {it.name}
    }

}