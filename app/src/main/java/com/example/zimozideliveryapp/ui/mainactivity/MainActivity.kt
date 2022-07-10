package com.example.zimozideliveryapp.ui.mainactivity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.zimozideliveryapp.R
import com.example.zimozideliveryapp.databinding.ActivityMainBinding
import com.example.zimozideliveryapp.ui.mainactivity.viewmodel.FloatingBtnState
import com.example.zimozideliveryapp.ui.mainactivity.viewmodel.ActivitySharedBehaviour
import com.example.zimozideliveryapp.ui.mainactivity.viewmodel.SharedViewModel
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ActivitySharedBehaviour {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: SharedViewModel
    private lateinit var badgeDrawable: BadgeDrawable
    private var numbersOfItemsInCart = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        viewModel.activityBehaviour = this
        numbersOfItemsInCart = viewModel.getNumbersOfItemsInCart(this)
        initFabBadgeDrawable()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        binding.cartFab.setOnClickListener {
            viewModel.openCart()
        }
    }

    private fun initFabBadgeDrawable() {
        binding.cartFab.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            @SuppressLint("UnsafeOptInUsageError")
            override fun onGlobalLayout() {
                badgeDrawable = BadgeDrawable.create(this@MainActivity)
                badgeDrawable.horizontalOffset = 25
                badgeDrawable.verticalOffset = 15
                badgeDrawable.backgroundColor = ContextCompat.getColor(this@MainActivity, R.color.green)
                BadgeUtils.attachBadgeDrawable(badgeDrawable, binding.cartFab, null)
                binding.cartFab.viewTreeObserver.removeOnGlobalLayoutListener(this)
                if (numbersOfItemsInCart == 0)
                    badgeDrawable.isVisible = false
            }
        })
    }

    override fun addMealToCart(id: Int) {
        if (numbersOfItemsInCart == 0) badgeDrawable.isVisible = true
        viewModel.saveNumbersOfItemsInCart(this, ++numbersOfItemsInCart)
        badgeDrawable.number = numbersOfItemsInCart

    }

    override fun changeFloatingActionButtonState(state: FloatingBtnState) {
        when (state) {
            FloatingBtnState.SHOW -> {

                binding.cartFab.apply {
                    isClickable = true
                    animate()
                        .alpha(1f)
                        .setDuration(200)
                        .start()

                }
            }

            FloatingBtnState.HIDE -> {

                binding.cartFab.apply {
                    isClickable = false
                    animate()
                        .alpha(0f)
                        .setDuration(200)
                        .start()
                }
            }
        }
    }

    override fun deleteFromCart() {
        if (numbersOfItemsInCart > 0) {
            viewModel.saveNumbersOfItemsInCart(this, --numbersOfItemsInCart)
            badgeDrawable.number = numbersOfItemsInCart
            if (numbersOfItemsInCart == 0) badgeDrawable.isVisible = false
        }
    }

}