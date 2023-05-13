package com.example.customfood.data.remote

object HttpRoutes {
    private const val BASE_URL = "http://192.168.1.166"
    const val PORT = 5000
    const val OPTIONS = "$BASE_URL/options"
    const val OPTION = "$BASE_URL/option"
    const val ITEMS = "$BASE_URL/items"
    const val ITEM = "$BASE_URL/item"
    const val RECIPE = "$BASE_URL/recipe"
    const val IMAGE = "$BASE_URL/image"
}