package com.example.customfood.data.remote

import android.util.Log
import com.example.customfood.data.remote.dto.*
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*

class RecipeServiceImplementation(
    private val client: HttpClient
) : IRecipeService {
    val TAG = "CustomFood - RecipeServiceImplementation"

    //TODO - remove the list when flipping to recipes
    override suspend fun getFoodChoices(foodType: String): List<DataFoodChoiceResponse> {
        Log.d(TAG, "in getFoodChoices")
        return client.get {
            url(HttpRoutes.TYPES)
            port = 8000
            parameter("foodType", foodType)
        }
        //TODO - implement error catching, rewatch end of the Ktor video
        /*
        return try {
            client.get {
                url(HttpRoutes.POSTS)
            }
        } catch (e: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${e.response.status.description}")
            DataRecipeResponse
        } catch (e: ClientRequestException) {
            // 4xx - response
            println("Error: ${e.response.status.description}")
            emptyList()
        } catch (e: ServerResponseException){
            // 5xx - response
            println("Error: ${e.response.status.description}")
            emptyList()
        } catch (e: Exception){
            println("Error: ${e.message}")
            emptyList()
        }
         */
    }

    override suspend fun createPost(postRequest: DataFoodChoicesRequest): DataFoodChoicesRequest? {
        return try {
            client.post<DataFoodChoicesRequest>() {
                url(HttpRoutes.TYPES)
                contentType(ContentType.Application.Json)
                body = postRequest
            }
        } catch (e: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${e.response.status.description}")
            null
        } catch (e: ClientRequestException) {
            // 4xx - response
            println("Error: ${e.response.status.description}")
            null
        } catch (e: ServerResponseException){
            // 5xx - response
            println("Error: ${e.response.status.description}")
            null
        } catch (e: Exception){
            println("Error: ${e.message}")
            null
        }
    }

}