package com.example.pawmepetadoptionapp.FosterDogs

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyFosterPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CurrentFostersFragment()
            1 -> PastFostersFragment()
            else -> throw IllegalArgumentException("Invalid tab index")
        }
    }
}
