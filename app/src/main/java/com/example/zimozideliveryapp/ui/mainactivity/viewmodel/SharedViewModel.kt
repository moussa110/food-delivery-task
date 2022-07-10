package com.example.zimozideliveryapp.ui.mainactivity.viewmodel

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.zimozideliveryapp.utils.NUM_OF_ITEMS_IN_CART_PREF

class SharedViewModel : ViewModel() {
    var activityBehaviour: ActivitySharedBehaviour? = null
    var fragmentBehaviour: FragmentSharedBehaviour? = null

    private var _fabState = FloatingBtnState.HIDE
    val fabState get() = _fabState

    fun addMealToCart(id: Int) {
        activityBehaviour?.addMealToCart(id)
    }

    fun changeFloatingActonBarState(state: FloatingBtnState) {
        _fabState = state
        activityBehaviour?.changeFloatingActionButtonState(state)
    }

    fun openCart(){
        fragmentBehaviour?.openCart()
    }

    fun getNumbersOfItemsInCart(context: Activity): Int {
        val sharedPref = context.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getInt(NUM_OF_ITEMS_IN_CART_PREF, 0)
    }

    fun saveNumbersOfItemsInCart(context: Activity, number: Int) {
        val sharedPref = context.getPreferences(Context.MODE_PRIVATE)
        sharedPref.edit().putInt(NUM_OF_ITEMS_IN_CART_PREF, number).apply()
    }
}

interface ActivitySharedBehaviour {
    fun addMealToCart(id: Int)
    fun changeFloatingActionButtonState(state: FloatingBtnState)
    fun deleteFromCart()
}

interface FragmentSharedBehaviour {
    fun openCart()
}
enum class FloatingBtnState {
    SHOW, HIDE
}