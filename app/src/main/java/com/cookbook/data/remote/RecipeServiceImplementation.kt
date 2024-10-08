package com.cookbook.data.remote

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.cookbook.data.remote.dto.*
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlin.coroutines.coroutineContext
import kotlin.system.exitProcess

class RecipeServiceImplementation(
    private val client: HttpClient
) : IRestAPIService {
    val TAG = "CookBook - RecipeServiceImplementation"

    override suspend fun getOptions(): DataOptionsResponse {
        Log.d(TAG, "in getOptions")
        return client.get {
            url(HttpRoutes.OPTIONS)
            port = HttpRoutes.PORT
        }
    }
    override suspend fun getOption(option: String): DataOptionsResponse {
        Log.d(TAG, "in getOption")
        return client.get {
            url(HttpRoutes.OPTION)
            port = HttpRoutes.PORT
            parameter("option", option)
        }
    }

    override suspend fun getItems(option: String): List<DataItem> {
        Log.d(TAG, "in getItems")
        return client.get {
            url(HttpRoutes.ITEMS)
            port = HttpRoutes.PORT
            parameter("option", option)
        }
    }
    override suspend fun getItem(item: String): DataItem {
        Log.d(TAG, "in getItem")
        return client.get {
            url(HttpRoutes.ITEM)
            port = HttpRoutes.PORT
            parameter("item", item)
        }
    }
    override suspend fun getRecipe(ingredients: List<String>, ignore: List<String>, userId: String, original: Boolean, random: Boolean): DataRecipe{
        Log.d(TAG, "in getRecipe")
        return client.get {
            url(HttpRoutes.RECIPE)
            port = HttpRoutes.PORT
            parameter("include", ingredients.toString())
            parameter("ignore", ignore.toString())
            parameter("user_id", userId)
            parameter("original", original)
            parameter("random", random)
        }
    }
    override suspend fun getImage(image: String): Bitmap {
        Log.d(TAG, "in getImage: " + image)
        Log.d(TAG, "Parent: ${coroutineContext[Job]}")
        try {
            val response = client.get<ByteArray> {
                url(HttpRoutes.IMAGE)
                parameter("image", image)
                port = HttpRoutes.PORT
                headers {
                    append(HttpHeaders.Accept, "image/gif")
                }
            }
            Log.d(TAG, "Got raw image: " + image)
            val bitmap = BitmapFactory.decodeByteArray(response, 0, response.size)
            client.close()

            return bitmap
        }catch(e: CancellationException){
            Log.d(TAG, "Throwing...")
            throw e
        }catch(e: Exception){
            Log.d(TAG, "Exception - Parent: ${kotlin.coroutines.coroutineContext[Job]}")
            exitProcess(-1)

        }
    }
    //TODO - remove the list when flipping to recipes
    /*
    override suspend fun getFoodChoices(option: String): List<DataItemResponse> {
        Log.d(TAG, "in getFoodChoices")
        return client.get {
            url(HttpRoutes.ITEM)
            port = HttpRoutes.PORT
            parameter("item", option)
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
    */

    override suspend fun createPost(postRequest: DataRequest): DataRequest? {
        //TODO - what does this do? Is it needed?
        return try {
            client.post<DataRequest>() {
                url(HttpRoutes.OPTION)
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

    override suspend fun saveRecipe(userId: String?, recipeId: String) : DataSaveRecipe {
        Log.d(TAG, "in saveRecipe")
        return client.post {
            url(HttpRoutes.SAVE)
            port = HttpRoutes.PORT
            parameter("userId", userId)
            parameter("recipeId", recipeId)
        }
    }

    override suspend fun deleteRecipe(userId: String?, recipeId: String) : DataSaveRecipe {
        Log.d(TAG, "in deleteRecipe")
        return client.delete {
            url(HttpRoutes.SAVE)
            port = HttpRoutes.PORT
            parameter("userId", userId)
            parameter("recipeId", recipeId)
        }
    }
}