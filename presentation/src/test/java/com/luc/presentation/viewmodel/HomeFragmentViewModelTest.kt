package com.luc.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.base.CharMatcher.`is`
import com.google.common.truth.Truth.assertThat
import com.luc.common.model.SongMetadata
import com.luc.common.resource.NetworkStatus
import com.luc.presentation.MainCoroutineRule
import com.luc.presentation.getOrAwaitValueTest
import com.luc.presentation.usecases.FakeGetAlbumsUseCaseImpl
import com.luc.presentation.usecases.FakeGetSongsUseCaseImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeFragmentViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var homeFragmentViewModel: HomeFragmentViewModel

    @Before
    fun setUp() {
        homeFragmentViewModel =
            HomeFragmentViewModel(FakeGetAlbumsUseCaseImpl(), FakeGetSongsUseCaseImpl())
    }

    @Test
    fun getAlbums() {
    }
}