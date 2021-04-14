package com.szareckii.nasapictureoftheday.ui.picture.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.szareckii.nasapictureoftheday.R
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment: Fragment() {

    private var textIsVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return

        chipGroupListener(sharedPref)

        button_small_size_text.setOnClickListener(clickListener(sharedPref))
        button_med_size_text.setOnClickListener(clickListener(sharedPref))
        button_big_size_text.setOnClickListener(clickListener(sharedPref))
    }

    private fun clickListener(sharedPref: SharedPreferences) = View.OnClickListener { view ->
        when (view) {
            button_small_size_text -> saveBtnSharedPref(sharedPref, "small")
            button_med_size_text -> saveBtnSharedPref(sharedPref, "med")
            button_big_size_text -> saveBtnSharedPref(sharedPref, "big")
        }
    }

    private fun saveBtnSharedPref(sharedPref: SharedPreferences, text: String) {
        with(sharedPref.edit()) {
            putString(getString(R.string.size_text), text)
            apply()
        }
        activity?.recreate()
    }

    private fun chipGroupListener(sharedPref: SharedPreferences) {
    chipGroup.setOnCheckedChangeListener { chipGroup, position ->
        chipGroup.findViewById<Chip>(position)?.let {
            saveThemeSharedPref(sharedPref, it.text.toString())
            }
        }
     }

    private fun saveThemeSharedPref(sharedPref: SharedPreferences, text: String) {
        if (text == getString(R.string.theme_day)) {
                with(sharedPref.edit()) {
                    putInt(getString(R.string.theme), 1)
                    apply()
                }
        } else {
                with(sharedPref.edit()) {
                    putInt(getString(R.string.theme), 2)
                    apply()
            }
        }
        activity?.recreate()
    }
}