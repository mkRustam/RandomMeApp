package com.mkr.randomuser.presentation.ui.details

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class UserDetailsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> UserDetailsPersonalFragment()
            1 -> UserDetailsLocationFragment()
            2 -> UserDetailsLoginFragment()
            else -> throw IllegalStateException("Invalid position")
        }
    }
}