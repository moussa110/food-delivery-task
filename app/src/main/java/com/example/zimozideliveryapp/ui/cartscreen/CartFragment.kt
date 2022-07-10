package com.example.zimozideliveryapp.ui.cartscreen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.zimozideliveryapp.R
import com.example.zimozideliveryapp.databinding.FragmentCartBinding
import com.example.zimozideliveryapp.ui.cartscreen.viewmodels.CartFragmentViewModel
import com.example.zimozideliveryapp.ui.mainactivity.viewmodel.SharedViewModel
import com.example.zimozideliveryapp.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart) {
    private lateinit var binding: FragmentCartBinding
    private lateinit var viewModel: CartFragmentViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var cartRecyclerAdapter: CartRecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCartBinding.bind(view)
        viewModel = ViewModelProvider(this)[CartFragmentViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        initCategoriesTabsLayout()
        subscribeToLiveData()
        initCartRecycler()
    }


    private fun initCartRecycler() {
        cartRecyclerAdapter = CartRecyclerAdapter {
            sharedViewModel.activityBehaviour?.deleteFromCart()
            viewModel.deleteMeal(it)
            updateTotalPrice(it.price)
        }
        binding.itemsRv.apply {
            adapter = cartRecyclerAdapter
        }
        viewModel.getAllMeals()
    }


    private fun subscribeToLiveData() {
        viewModel.mealsLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.LOADING -> {

                }
                Resource.Status.SUCCESS -> {
                    cartRecyclerAdapter.submitList(it.data)
                    updateTotalPrice()
                }
                else -> {}
            }
        }
    }

    private fun updateTotalPrice(p: Int = 0) {
        var price = 0
        for (i in cartRecyclerAdapter.currentList)
            price += i.price
        price -= p
        binding.totalCostTv.text = "$price USD"
    }

    private fun initCategoriesTabsLayout() {
        binding.apply {
            val list = resources.getStringArray(R.array.cart_list)
            for (i in list.indices) {
                if (i == 0)
                    tabLayout.addTab(tabLayout.newTab().setText(list[i]), true)
                else
                    tabLayout.addTab(tabLayout.newTab().setText(list[i]))
            }
        }
    }
}