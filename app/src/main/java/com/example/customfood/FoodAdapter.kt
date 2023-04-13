package com.example.customfood

import android.content.ClipData
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView

class FoodAdapter(
    val foods: List<Food>
) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    val TAG = "CustomFoodTAG"

    inner class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val checkBox : CheckBox = itemView.findViewById<CheckBox>(R.id.cv_selected)
        fun bind(item: Food) {
            checkBox.text = item.title
            checkBox.isChecked = item.isChecked
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                item.isChecked = isChecked
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
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
        holder.bind(foods[position])
    }

    override fun getItemCount(): Int {
        return foods.size
    }

    fun getCheckedItems(): List<String> {
        return foods.filter { it.isChecked }.map {it.title}
    }

}