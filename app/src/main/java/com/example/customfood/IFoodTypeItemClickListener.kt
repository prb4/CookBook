package com.example.customfood

import com.example.customfood.data.remote.dto.DataItemResponse
import com.example.customfood.data.remote.dto.DataResponse

interface IFoodTypeItemClickListener {
    //Interface to return the choice from the AdapterFoodType
    fun onFoodTypeItemClick(foodType: DataFoodType, data: List<DataItemResponse>)
}