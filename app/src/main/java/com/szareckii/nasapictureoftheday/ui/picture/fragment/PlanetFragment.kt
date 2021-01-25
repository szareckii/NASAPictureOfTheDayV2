package com.szareckii.nasapictureoftheday.ui.picture.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import coil.api.load
import com.szareckii.nasapictureoftheday.R
import com.szareckii.nasapictureoftheday.ui.picture.ViewPagerAdapter
import com.szareckii.nasapictureoftheday.ui.picture.viewmodel.epic.EPICPictureData
import com.szareckii.nasapictureoftheday.ui.picture.viewmodel.epic.EPICPictureViewModel
import com.szareckii.nasapictureoftheday.ui.picture.viewmodel.mars.PictureOfTheMarsData
import com.szareckii.nasapictureoftheday.ui.picture.viewmodel.mars.PictureOfTheMarsViewModel
import kotlinx.android.synthetic.main.bottom_sheet_layout.*
import kotlinx.android.synthetic.main.fragment_earth.*
import kotlinx.android.synthetic.main.fragment_mars.*
import kotlinx.android.synthetic.main.fragment_pictureoftheday.*
import kotlinx.android.synthetic.main.fragment_planet.*
import java.io.*


class PlanetFragment: Fragment() {

    companion object {
        private const val EARTH = 0
        private const val MARS = 1
    }

    //Ленивая инициализация модели
    private val viewModelMars: PictureOfTheMarsViewModel by lazy {
        ViewModelProviders.of(this).get(PictureOfTheMarsViewModel::class.java)
    }

    private val viewModelEPIC: EPICPictureViewModel by lazy {
        ViewModelProviders.of(this).get(EPICPictureViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_planet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelMars.getData()
            .observe(viewLifecycleOwner, { renderDataMars(it) })

        viewModelEPIC.getData()
            .observe(viewLifecycleOwner, { renderDataEPIC(it) })

        view_pager.adapter = ViewPagerAdapter(childFragmentManager)

        tab_layout.setupWithViewPager(view_pager)
        tab_layout.getTabAt(0)?.setIcon(R.drawable.ic_earth)
        tab_layout.getTabAt(1)?.setIcon(R.drawable.ic_planets)

        setCustomTabs()
        setHighlightedTab(EARTH)

        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {
                setHighlightedTab(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
            ) {
            }
        })
    }

    private fun setHighlightedTab(position: Int) {
        val layoutInflater = LayoutInflater.from(activity)

        tab_layout.getTabAt(EARTH)?.customView = null
        tab_layout.getTabAt(MARS)?.customView = null

        when (position) {
            EARTH -> {
                setEarthTabHighlighted(layoutInflater)
            }
            MARS -> {
                setMarsTabHighlighted(layoutInflater)
            }
            else -> {
                setEarthTabHighlighted(layoutInflater)
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun setEarthTabHighlighted(layoutInflater: LayoutInflater) {
        val earth =
            layoutInflater.inflate(R.layout.activity_api_custom_tab_earth, null)
        earth.findViewById<AppCompatTextView>(R.id.tab_image_textview)
            .setTextColor(
                    ContextCompat.getColor(requireActivity(), R.color.colorAccent)
            )
        tab_layout.getTabAt(EARTH)?.customView = earth
        tab_layout.getTabAt(MARS)?.customView =
            layoutInflater.inflate(R.layout.activity_api_custom_tab_mars, null)
    }

    @SuppressLint("InflateParams")
    private fun setMarsTabHighlighted(layoutInflater: LayoutInflater) {
        val mars =
            layoutInflater.inflate(R.layout.activity_api_custom_tab_mars, null)
        mars.findViewById<AppCompatTextView>(R.id.tab_image_textview)
            .setTextColor(
                    ContextCompat.getColor(requireActivity(), R.color.colorAccent)
            )
        tab_layout.getTabAt(EARTH)?.customView =
            layoutInflater.inflate(R.layout.activity_api_custom_tab_earth, null)
        tab_layout.getTabAt(MARS)?.customView = mars
    }

    @SuppressLint("InflateParams")
    private fun setCustomTabs() {
        val layoutInflater = LayoutInflater.from(activity)
        tab_layout.getTabAt(0)?.customView =
            layoutInflater.inflate(R.layout.activity_api_custom_tab_earth, null)
        tab_layout.getTabAt(1)?.customView =
            layoutInflater.inflate(R.layout.activity_api_custom_tab_mars, null)
    }

    private fun renderDataMars(data: PictureOfTheMarsData) {
        when (data) {
            is PictureOfTheMarsData.Success -> {
                Log.e("11111111111111111", "renderDataMars.Success")

                val serverResponseData = data.serverResponseData
                val url = serverResponseData.photos?.get(0)?.img_src
                if (url.isNullOrEmpty()) {
                    toast("Link is empty")
                } else {
                    Log.e("11111111111111111", "renderDataMars url NoNullOrEmpty")
                    circularProgressbar_mars.visibility = View.GONE
                    image_earth_view.visibility = View.VISIBLE

                    image_mars_view.load(url) {
                        lifecycle(this@PlanetFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                }
            }
            is PictureOfTheMarsData.Loading -> {
                //showLoading()
            }
            is PictureOfTheMarsData.Error -> {
                toast(data.error.message)
            }
        }
    }

    private fun renderDataEPIC(data: EPICPictureData) {
        when (data) {
            is EPICPictureData.Success -> {
                Log.e("11111111111111111", "renderDataEPIC.Success")

                val serverResponseData = data.serverResponseData
                if (serverResponseData == null) {
                    toast("Link is empty")
                } else {
                    circularProgressbar_earth.visibility = View.GONE
                    image_earth_view.visibility = View.VISIBLE
                    image_earth_view.load(serverResponseData) {
                        lifecycle(this@PlanetFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                }
            }
            is EPICPictureData.Loading -> {
                //showLoading()
            }
            is EPICPictureData.Error -> {
                toast(data.error.message)
            }
        }
    }

    private fun Fragment.toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 250)
            show()
        }
    }
}