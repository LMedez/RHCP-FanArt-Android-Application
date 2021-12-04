package com.luc.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.luc.home.albumsfragment.AlbumsFragment
import com.luc.home.mymusicfragment.MyMusicFragment
import com.luc.home.overviewfragment.OverviewFragment

class ViewPagerAdapter(f: Fragment) : FragmentStateAdapter(f) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> OverviewFragment()
            1 -> AlbumsFragment()
            2 -> MyMusicFragment()
            else -> OverviewFragment()
        }
    }
}