package com.example.customfood

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.customfood.data.remote.dto.DataPostResponse
import com.example.customfood.data.remote.dto.IRecipeService
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import androidx.lifecycle.lifecycleScope
import com.example.customfood.ui.WebRequests

class AdapterFoodType(
    val options: List<DataFoodType>,
    private val foodTypeClickListener: IFoodTypeItemClickListener
) : RecyclerView.Adapter<AdapterFoodType.OptionViewHolder>() {

    val TAG = "CustomFood - OptionAdapter.kt"
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

            val webRequests = WebRequests()
            webRequests.startRequest(options[position].title)

            foodTypeClickListener.onFoodTypeItemClick(options[position])
        }
    }
    override fun getItemCount(): Int {
        return options.size
    }
}