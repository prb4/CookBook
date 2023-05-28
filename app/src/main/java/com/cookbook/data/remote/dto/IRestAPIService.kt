package com.cookbook.data.remote.dto

import android.graphics.Bitmap
import com.cookbook.data.remote.RecipeServiceImplementation
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*

interface IRestAPIService {

    //suspend fun getFoodChoices(foodType: String): List<DataItemResponse>

    suspend fun createPost(postRequest: DataRequest): DataRequest?

    suspend fun getOptions(): DataOptionsResponse

    suspend fun getItems(option: String): List<DataItem>

    suspend fun getItem(item: String): DataItem

    suspend fun getOption(option: String): DataOptionsResponse

    suspend fun getImage(image: String): Bitmap

    //TODO - userId should never be null
    suspend fun saveRecipe(userId: String?, recipeId: String) : DataSaveRecipe

    //TODO - userId should never be null
    suspend fun deleteRecipe(userId: String?, recipeId: String) : DataSaveRecipe

    suspend fun getRecipe(
        ingredients: List<String>,
        ignoreIngredients: List<String>,
        userId: String,
        original: Boolean,
        random: Boolean
    ):DataRecipe
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