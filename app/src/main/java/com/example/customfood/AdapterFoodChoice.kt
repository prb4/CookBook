package com.example.customfood

import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class AdapterFoodChoice(
    val dataFoodChoices: List<DataFoodChoice>
) : RecyclerView.Adapter<AdapterFoodChoice.FoodViewHolder>() {

    val TAG = "CustomFood - AdapterFoodChoice.kt"

    inner class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val checkBox : CheckBox = itemView.findViewById<CheckBox>(R.id.cv_selected)
        fun bind(item: DataFoodChoice) {
            Log.d(TAG, "in FoodViewHolder.bind")
            checkBox.text = item.title
            checkBox.isChecked = item.isChecked
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                item.isChecked = isChecked
            }
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
        holder.bind(dataFoodChoices[position])
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "in getItemCount")
        return dataFoodChoices.size
    }

    fun getCheckedItems(): List<String> {
        Log.d(TAG, "in getCheckedItems")
        return dataFoodChoices.filter { it.isChecked }.map {it.title}
    }

}