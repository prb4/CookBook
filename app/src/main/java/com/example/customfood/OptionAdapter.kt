package com.example.customfood

import android.content.ClipData
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class OptionAdapter(
    val options: List<FoodOption>,
    private val itemClickListener: IFoodOptionsItemClickListener
) : RecyclerView.Adapter<OptionAdapter.OptionViewHolder>() {

    val TAG = "CustomFoodTAG-OptionAdapter.kt"

    inner class OptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return OptionViewHolder(view)
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder")
        //TODO - how to display the image
        Log.d(TAG, "Position: ${position}, Title: ${options[position].title}")
        //TODO - Make the cardview's closer together, maybe move away from a constraint layout?
        holder.itemView.findViewById<TextView>(R.id.tv_description).text = options[position].title
        holder.itemView.findViewById<ImageView>(R.id.iv_img).setImageResource(options[position].image)

        holder.itemView.setOnClickListener{
            //TODO - Seems like some clicks aren't registered. Maybe the click is landing on the textview?
            Log.d(TAG, "Clicked on ${options[position].title}")
            itemClickListener.onFoodOptionsItemClick(options[position].title)
        }
    }

    override fun getItemCount(): Int {
        return options.size
    }
}