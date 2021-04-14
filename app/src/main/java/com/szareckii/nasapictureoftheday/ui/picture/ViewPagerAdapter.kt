package com.szareckii.nasapictureoftheday.ui.picture

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.szareckii.nasapictureoftheday.ui.picture.fragment.EarthFragment
import com.szareckii.nasapictureoftheday.ui.picture.fragment.MarsFragment

class ViewPagerAdapter(fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager) {

    companion object {
        private const val EARTH_FRAGMENT = 0
        private const val MARS_FRAGMENT = 1
    }

    private val fragments = arrayOf(EarthFragment(), MarsFragment())

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> fragments[EARTH_FRAGMENT]
            1 -> fragments[MARS_FRAGMENT]
            else -> fragments[EARTH_FRAGMENT]
        }
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Earth"
            1 -> "Mars"
            else -> "Earth"
        }
    }
}
