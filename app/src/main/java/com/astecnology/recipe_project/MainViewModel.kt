package com.astecnology.recipe_project

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astecnology.recipe_project.Data.Category
import com.astecnology.recipe_project.Data.CategoriesResponse
import com.astecnology.recipe_project.Data.Meal
import com.astecnology.recipe_project.Data.MealDetail
import com.astecnology.recipe_project.Data.MealDetailResponse
import com.astecnology.recipe_project.Data.MealsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    val categories = MutableLiveData<List<Category>>()
    val meals = MutableLiveData<List<Meal>>()
    private val _mealDetail = MutableLiveData<MealDetail?>()
    val mealDetail: LiveData<MealDetail?> = _mealDetail

    fun fetchCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            val call = RetrofitClient.apiService.getCategories()

            call.enqueue(object : Callback<CategoriesResponse> {
                override fun onResponse(call: Call<CategoriesResponse>, response: Response<CategoriesResponse>) {
                    if (response.isSuccessful) {
                        Log.d("100akash",response.toString())
                        categories.postValue(response.body()?.categories)
                    }
                }

                override fun onFailure(call: Call<CategoriesResponse>, t: Throwable) {
                    // Handle failure
                }
            })
        }
    }

    fun fetchFilteredMeals(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val call = RetrofitClient.apiService.filterMeals(category)
            call.enqueue(object : Callback<MealsResponse> {
                override fun onResponse(call: Call<MealsResponse>, response: Response<MealsResponse>) {
                    if (response.isSuccessful) {
                        meals.postValue(response.body()?.meals)
                    }
                }

                override fun onFailure(call: Call<MealsResponse>, t: Throwable) {
                    // Handle failure
                }
            })
        }
    }

    fun fetchMealDetail(mealId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val call = RetrofitClient.apiService.getMealDetail(mealId)
                call.enqueue(object : Callback<MealDetailResponse> {
                    override fun onResponse(call: Call<MealDetailResponse>, response: Response<MealDetailResponse>) {
                        if (response.isSuccessful) {
                            val mealDetailResponse = response.body()
                            if (mealDetailResponse?.meals != null && mealDetailResponse.meals.isNotEmpty()) {
                                _mealDetail.postValue(mealDetailResponse.meals.first())
                            } else {
                                Log.e("MainViewModel", "Meal details are empty or null")
                                _mealDetail.postValue(null)
                            }
                        } else {
                            Log.e("MainViewModel", "Failed to fetch MealDetail: ${response.errorBody()}")
                            _mealDetail.postValue(null)
                        }
                    }

                    override fun onFailure(call: Call<MealDetailResponse>, t: Throwable) {
                        Log.e("MainViewModel", "Error fetching MealDetail", t)
                        _mealDetail.postValue(null)
                    }
                })
            } catch (e: Exception) {
                Log.e("MainViewModel", "Exception in fetchMealDetail", e)
                _mealDetail.postValue(null)
            }
        }
    }
}
