package com.cookbook

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cookbook.R

class AdapterRecipe(
    val dataText: List<String>,
) : RecyclerView.Adapter<AdapterRecipe.RecipeViewHolder>() {
    val TAG = "CookBook - AdapterRecipe.kt"

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_text, parent, false)
        return RecipeViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "in getItemCount: ${dataText.size.toString()}")
        return dataText.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder")
        //TODO - how to display the image
        Log.d(TAG, "Position: ${position}, Title: ${dataText[position]}")
        //TODO - Make the cardview's closer together, maybe move away from a constraint layout?
        holder.itemView.findViewById<TextView>(R.id.cb_text).text = dataText[position]

    }
}