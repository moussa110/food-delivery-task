package com.example.zimozideliveryapp.ui.cartscreen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zimozideliveryapp.data.pojo.Meal
import com.example.zimozideliveryapp.repositories.MealsRepo
import com.example.zimozideliveryapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartFragmentViewModel @Inject constructor(private val repository: MealsRepo) : ViewModel() {
    private var _mealsLiveData = MutableLiveData<Resource<List<Meal>>>()
    val mealsLiveData: LiveData<Resource<List<Meal>>> get() = _mealsLiveData

    fun getAllMeals() = viewModelScope.launch {
        _mealsLiveData.value = Resource.loading()
        _mealsLiveData.value = Resource.success(repository.getAllMealsInCart())
    }

    fun deleteMeal(meal: Meal) = viewModelScope.launch {
        repository.deleteMealFromCart(meal)
    }

    fun insertMeal(meal: Meal) = viewModelScope.launch {
        repository.insertMealToCart(meal)
    }


}