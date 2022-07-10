package com.example.zimozideliveryapp.ui.homescreen

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.zimozideliveryapp.R
import com.example.zimozideliveryapp.data.pojo.Meal
import com.example.zimozideliveryapp.databinding.FragmentHomeBinding
import com.example.zimozideliveryapp.ui.homescreen.adapters.MealsRecyclerAdapter
import com.example.zimozideliveryapp.ui.homescreen.adapters.SalePagerAdapter
import com.example.zimozideliveryapp.ui.homescreen.viewmodels.HomeFragmentViewModel
import com.example.zimozideliveryapp.ui.mainactivity.viewmodel.FloatingBtnState
import com.example.zimozideliveryapp.ui.mainactivity.viewmodel.FragmentSharedBehaviour
import com.example.zimozideliveryapp.ui.mainactivity.viewmodel.SharedViewModel
import com.example.zimozideliveryapp.utils.Resource
import com.google.android.material.tabs.TabLayout
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), FragmentSharedBehaviour {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var pagerAdapter: SalePagerAdapter
    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var mealsAdapter: MealsRecyclerAdapter
    private var pagerHandler: Handler = Handler(Looper.getMainLooper())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        viewModel = ViewModelProvider(this)[HomeFragmentViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        sharedViewModel.fragmentBehaviour = this
        subscribeToLiveData()
        initMealsRecycler()
        initCategoriesTabsLayout()
        binding.scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > 450 && sharedViewModel.fabState == FloatingBtnState.HIDE)
                sharedViewModel.changeFloatingActonBarState(FloatingBtnState.SHOW)
            else if (scrollY < 450 && sharedViewModel.fabState == FloatingBtnState.SHOW)
                sharedViewModel.changeFloatingActonBarState(FloatingBtnState.HIDE)
        })
    }


    private fun initMealsRecycler() {
        mealsAdapter = MealsRecyclerAdapter { addMealToCart(it) }
        binding.itemsRv.apply {
            adapter = mealsAdapter
        }
    }

    private fun addMealToCart(meal: Meal) {
        sharedViewModel.addMealToCart(meal.id)
        viewModel.addToCart(meal)
    }

    private fun subscribeToLiveData() {
        viewModel.mealsLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.LOADING -> {
                    binding.progress.visibility = View.VISIBLE
                }
                Resource.Status.ERROR -> {
                    binding.progress.visibility = View.GONE
                    Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                }
                Resource.Status.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    mealsAdapter.submitList(it.data!!.meals)
                }
            }
        }
    }

    private fun initCategoriesTabsLayout() {
        binding.apply {
            val list = resources.getStringArray(R.array.categories)
            for (i in list.indices) {
                if (i == 0)
                    tabLayout.addTab(tabLayout.newTab().setText(list[i]), true)
                else
                    tabLayout.addTab(tabLayout.newTab().setText(list[i]))
            }
            tabLayout.apply {
                viewModel.getMealsByCategory(
                    getTabAt(selectedTabPosition)?.text.toString(),
                    requireContext()
                )
            }

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tabLayout.apply {

                        mealsAdapter.submitList(listOf())
                        viewModel.getMealsByCategory(
                            getTabAt(selectedTabPosition)?.text.toString(),
                            requireContext()
                        )
                    }
                }


                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

            })
        }

    }

    private fun initSaleViewPager() {
        pagerAdapter = SalePagerAdapter()
        binding.apply {
            viewPager.adapter = pagerAdapter
            pagerAdapter.updateAllData(
                listOf(
                    R.drawable.mega_sale_background,
                    R.drawable.mega_sale_background,
                    R.drawable.mega_sale_background
                )
            )
            viewPager.autoScroll(3)
        }
    }

    fun ViewPager2.autoScroll(durationBySeconds: Int) {
        this.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                handler.removeMessages(0)

                val runnable = Runnable {
                    if (currentItem == pagerAdapter.itemCount - 1) {
                        currentItem = 0
                        return@Runnable
                    }
                    this@autoScroll.currentItem = ++this@autoScroll.currentItem
                    binding.dotsIndicator.attachTo(this@autoScroll)
                }
                if (position < (this@autoScroll.adapter?.itemCount ?: 0)) {
                    binding.dotsIndicator.attachTo(this@autoScroll)
                    pagerHandler.postDelayed(runnable, durationBySeconds * 1000L)
                }
            }
        })
    }


    override fun onResume() {
        super.onResume()
        initSaleViewPager()
    }

    override fun onPause() {
        super.onPause()
        pagerHandler.removeCallbacksAndMessages(null)
    }

    override fun openCart() {
        findNavController().navigate(R.id.action_homeFragment_to_cartFragment)
    }
}