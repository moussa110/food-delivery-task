package com.example.zimozideliveryapp.ui.homescreen.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zimozideliveryapp.data.pojo.Meal
import com.example.zimozideliveryapp.data.pojo.MealsResponse
import com.example.zimozideliveryapp.repositories.MealsRepo
import com.example.zimozideliveryapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(val repository: MealsRepo) : ViewModel() {
    private var _mealsLiveData = MutableLiveData<Resource<MealsResponse>>()
    val mealsLiveData: LiveData<Resource<MealsResponse>> get() = _mealsLiveData

    fun getMealsByCategory(name: String, context: Context) = viewModelScope.launch {
        _mealsLiveData.value = Resource.loading()
        val response = repository.getMealsByCategoryName(name = name, context)
        if (response == null) _mealsLiveData.value = Resource.error("error happened !!")
        else _mealsLiveData.value = Resource.success(response)
    }

    fun addToCart(meal: Meal) = viewModelScope.launch {
        repository.insertMealToCart(meal)
    }
}