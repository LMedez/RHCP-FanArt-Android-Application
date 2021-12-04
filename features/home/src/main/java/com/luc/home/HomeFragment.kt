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

        musicDataViewModel.getAlbums()
        musicDataViewModel.getLimitSongs()
        musicDataViewModel.albums.observe(viewLifecycleOwner){}

        musicDataViewModel.networkError.observe(viewLifecycleOwner) {
            if (it == null) {
                binding.progressBar.show()
                binding.offlineErrorView.hide()
            } else {
                binding.progressBar.hide()
                binding.offlineErrorView.show(true)
            }
        }

        musicDataViewModel.loadingState.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressBar.show()
                binding.contentContainer.hide()
            } else {
                binding.offlineErrorView.hide()
                binding.progressBar.hide()
                binding.contentContainer.show(false)
            }
        }

        binding.retryButton.setOnClickListener {
            musicDataViewModel.getLimitSongs()
            musicDataViewModel.getAlbums()
        }

        binding.viewPager.isUserInputEnabled = false
    }

}