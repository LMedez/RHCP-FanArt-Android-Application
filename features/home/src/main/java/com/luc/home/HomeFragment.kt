package com.luc.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.doOnPreDraw
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.MaterialSharedAxis
import com.luc.core.BaseFragment
import com.luc.home.databinding.FragmentHomeBinding
import com.luc.presentation.viewmodel.MusicDataViewModel
import com.luc.resources.animation.hide
import com.luc.resources.animation.show
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private lateinit var adapter: ViewPagerAdapter
    private val musicDataViewModel: MusicDataViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        exitTransition =
            MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true).addTarget(binding.root)
        reenterTransition =
            MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false).addTarget(binding.root)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        adapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Overview"
                }

                1 -> {
                    tab.text = "Albums"
                }

                else -> tab.text = "My music"

            }
        }.attach()

        musicDataViewModel.networkError.observe(viewLifecycleOwner) {
            binding.contentContainer.hide()
            binding.offlineErrorView.show(true)
        }

        binding.viewPager.isUserInputEnabled = false
    }

}