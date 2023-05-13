package com.example.customfood.data.remote.dto

import android.graphics.Bitmap
import com.example.customfood.data.remote.RecipeServiceImplementation
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*

interface IRestAPIService {

    //suspend fun getFoodChoices(foodType: String): List<DataItemResponse>

    suspend fun createPost(postRequest: DataRequest): DataRequest?

    suspend fun getOptions(): List<DataOptionsResponse>

    suspend fun getItems(option: String): List<DataItemResponse>

    suspend fun getItem(item: String): DataItemResponse

    suspend fun getOption(option: String): DataOptionsResponse

    suspend fun getImage(image: String): Bitmap

    suspend fun getRecipe(
        ingredients: List<String>,
        ignoreIngredients: List<String>,
        userId: String,
        original: Boolean
    ){

    }
    companion object {
        fun create(): IRestAPIService {
            return RecipeServiceImplementation(
                client = HttpClient(Android) {
                    install(Logging) {
                        level = LogLevel.ALL
                    }
                install(JsonFeature) {
                    serializer = KotlinxSerializer()
                }
                }
            )
        }
    }
}