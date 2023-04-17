package com.example.customfood.data.remote.dto

import com.example.customfood.data.remote.RecipeServiceImplementation
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*

interface IRecipeService {

    suspend fun getFoodChoices(foodType: String): List<DataFoodChoiceResponse>

    suspend fun createPost(postRequest: DataFoodChoicesRequest): DataFoodChoicesRequest?

    companion object {
        fun create(): IRecipeService {
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