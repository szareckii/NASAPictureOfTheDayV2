package com.szareckii.nasapictureoftheday.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.szareckii.nasapictureoftheday.R
import com.szareckii.nasapictureoftheday.ui.picture.fragment.PictureOfTheDayFragment
import com.szareckii.nasapictureoftheday.ui.picture.fragment.PlanetFragment
import com.szareckii.nasapictureoftheday.ui.picture.fragment.SettingsFragment
import kotlinx.android.synthetic.main.bottom_sheet_layout.*
import kotlinx.android.synthetic.main.fragment_pod_start.*
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTheme()
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.activity_api_bottom_container, PictureOfTheDayFragment.newInstance())
                    .commitNow()
        }
        setBottomNavigation()
    }

    private fun setBottomNavigation() {
        bottom_navigation_view.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_view_earth -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.activity_api_bottom_container, PictureOfTheDayFragment())
                        .commitNow()

                    true
                }
                R.id.bottom_view_planet -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.activity_api_bottom_container, PlanetFragment())
                        .commitNow()
                    true
                }
                R.id.bottom_view_settings -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.activity_api_bottom_container, SettingsFragment())
                        .commitNow()
                    true
                }
                else -> false
            }
        }
    }

    private fun initTheme() {

        sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return

        when(sharedPref.getInt(getString(R.string.theme), 1)) {
            1 -> initTextSize()
            2 -> initTextSizeSpace()
        }
    }

    private fun initTextSize() {
        when(sharedPref.getString(getString(R.string.size_text), "big")) {
            "small" -> setTheme(R.style.FontSizeSmall)
            "med" -> setTheme(R.style.FontSizeMedium)
            "big" -> setTheme(R.style.FontSizeLarge)
        }
    }

    private fun initTextSizeSpace() {
        when(sharedPref.getString(getString(R.string.size_text), "big")) {
            "small" -> setTheme(R.style.FontSizeSmallSpace)
            "med" -> setTheme(R.style.FontSizeMediumSpace)
            "big" -> setTheme(R.style.FontSizeLargeSpace)
        }
    }
}