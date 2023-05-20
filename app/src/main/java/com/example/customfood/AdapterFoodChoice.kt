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
import com.example.customfood.data.remote.dto.DataItem

class AdapterFoodChoice(
    val dataItemResponses: List<DataItem>
) : RecyclerView.Adapter<AdapterFoodChoice.FoodViewHolder>() {

    val TAG = "CustomFood - AdapterFoodChoice.kt"

    inner class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val checkBox : CheckBox = itemView.findViewById<CheckBox>(R.id.cv_selected)
        val description: TextView = itemView.findViewById<CheckBox>(R.id.tv_description)
        val imageView: ImageView = itemView.findViewById<ImageView>(R.id.iv_img)
        val cardView: CardView = itemView.findViewById<CardView>(R.id.cv)
        fun bind(item: DataItem) {
            val context: Context = itemView.context
            Log.d(TAG, "in FoodViewHolder.bind")

            description.text = item.name

            imageView.setImageBitmap(ManageDataSingleton.objectImageDict.get(item.name) as Bitmap)
            setBackground(item)
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
                        Color.WHITE
                    )
                }
                Log.d(TAG, "${item.name} was checked to ${item.isChecked}")
            }
        }

        fun setBackground(item: DataItem, ) {
            val context: Context = itemView.context
            if (item.isChecked.equals("selected")) {
                cardView.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.light_green
                    )
                )
            } else if (item.isChecked.equals("ignore")) {
                cardView.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.light_red
                    )
                )
            } else if (item.isChecked.equals("blank")) {
                cardView.setCardBackgroundColor(
                    Color.WHITE
                )
            }
            Log.d(TAG, "${item.name} was checked to ${item.isChecked}")
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

    fun getSelectedItems(): List<DataItem> {
        Log.d(TAG, "in getCheckedItems")
        return dataItemResponses.filter { it.isChecked.equals("selected") }.map {it}
    }

    fun getIgnoreItems(): List<DataItem> {
        Log.d(TAG, "in getCheckedItems")
        return dataItemResponses.filter { it.isChecked.equals("ignore") }.map {it}
    }

}