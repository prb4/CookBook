package com.example.customfood

import com.example.customfood.data.remote.dto.DataRecipeResponse

interface IFoodTypeItemClickListener {
    //Interface to return the choice from the AdapterFoodType
    fun onFoodTypeItemClick(foodType: DataFoodType, data: DataRecipeResponse)
}