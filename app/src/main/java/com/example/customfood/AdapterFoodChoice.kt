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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AdapterFoodChoice(
    val dataItemResponses: List<DataItemResponse>
) : RecyclerView.Adapter<AdapterFoodChoice.FoodViewHolder>() {

    val TAG = "CustomFood - AdapterFoodChoice.kt"

    inner class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val checkBox : CheckBox = itemView.findViewById<CheckBox>(R.id.cv_selected)
        val imageView : ImageView = itemView.findViewById<ImageView>(R.id.iv_img)
        fun bind(item: DataItemResponse) {
            Log.d(TAG, "in FoodViewHolder.bind")
            var image = Bitmap.createBitmap(0, 0, Bitmap.Config.ARGB_8888)
            //val dataFoodType = mutableListOf<DataFoodType>()
            checkBox.text = item.name

            val job = GlobalScope.launch (Dispatchers.Default) {
                Log.d(TAG, "Downloading image: " + item.image)
                image = downloadImage(item.image)
                //dataFoodType.add(DataFoodType(item.name, image))
            }
            runBlocking {
                job.join()
                job.cancel()
                Log.d(TAG, "Successfully downloaded image for " + item.name)
            }


            imageView.setImageBitmap(image)
            checkBox.isChecked = item.isChecked.equals("true")
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                item.isChecked = item.isChecked
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