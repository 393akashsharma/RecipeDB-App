// ApiService.kt
package com.astecnology.recipe_project

import com.astecnology.recipe_project.Data.CategoriesResponse
import com.astecnology.recipe_project.Data.MealDetailResponse
import com.astecnology.recipe_project.Data.MealsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("categories.php")
    fun getCategories(): Call<CategoriesResponse>

    @GET("filter.php")
    fun filterMeals(@Query("c") category: String): Call<MealsResponse>

    @GET("lookup.php")
    fun getMealDetail(@Query("i") mealId: String): Call<MealDetailResponse>
}
