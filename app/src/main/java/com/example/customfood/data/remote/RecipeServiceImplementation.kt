package com.example.customfood.data.remote

import android.util.Log
import com.example.customfood.data.remote.dto.DataRecipeRequest
import com.example.customfood.data.remote.dto.DataRecipeResponse
import com.example.customfood.data.remote.dto.IRecipeService
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*

class RecipeServiceImplementation(
    private val client: HttpClient
) : IRecipeService {
    val TAG = "CustomFood - RecipeServiceImplementation"

    //TODO - remove the list when flipping to recipes
    override suspend fun getPost(): DataRecipeResponse {
        Log.d(TAG, "in getPost")
        return client.get {
            url(HttpRoutes.POSTS)
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

    override suspend fun createPost(postRequest: DataRecipeRequest): DataRecipeRequest? {
        return try {
            client.post<DataRecipeRequest>() {
                url(HttpRoutes.POSTS)
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