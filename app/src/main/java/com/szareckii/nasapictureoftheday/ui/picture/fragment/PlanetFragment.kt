package com.szareckii.nasapictureoftheday.ui.picture.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.transition.ChangeBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import coil.api.load
import com.szareckii.nasapictureoftheday.R
import com.szareckii.nasapictureoftheday.ui.picture.ViewPagerAdapter
import com.szareckii.nasapictureoftheday.ui.picture.viewmodel.epic.EPICPictureData
import com.szareckii.nasapictureoftheday.ui.picture.viewmodel.epic.EPICPictureViewModel
import com.szareckii.nasapictureoftheday.ui.picture.viewmodel.mars.PictureOfTheMarsData
import com.szareckii.nasapictureoftheday.ui.picture.viewmodel.mars.PictureOfTheMarsViewModel
import kotlinx.android.synthetic.main.activity_api_custom_tab_earth.*
import kotlinx.android.synthetic.main.activity_api_custom_tab_mars.*
import kotlinx.android.synthetic.main.bottom_sheet_layout.*
import kotlinx.android.synthetic.main.fragment_earth_end.*
import kotlinx.android.synthetic.main.fragment_earth_start.*
import kotlinx.android.synthetic.main.fragment_earth_start.circularProgressbar_earth
import kotlinx.android.synthetic.main.fragment_earth_start.fab_earth
import kotlinx.android.synthetic.main.fragment_earth_start.image_earth_view
import kotlinx.android.synthetic.main.fragment_earth_start.motion_layout_earth_alpha
import kotlinx.android.synthetic.main.fragment_earth_start.earth_option_one_container
import kotlinx.android.synthetic.main.fragment_mars_end.*
import kotlinx.android.synthetic.main.fragment_mars_start.*
import kotlinx.android.synthetic.main.fragment_mars_start.circularProgressbar_mars
import kotlinx.android.synthetic.main.fragment_mars_start.fab_mars
import kotlinx.android.synthetic.main.fragment_mars_start.image_mars_view
import kotlinx.android.synthetic.main.fragment_mars_start.motion_layout_mars_alpha
import kotlinx.android.synthetic.main.fragment_mars_start.mars_option_one_container
import kotlinx.android.synthetic.main.fragment_planet.*
import kotlinx.android.synthetic.main.fragment_pod_start.*
import java.io.*


class PlanetFragment: Fragment() {

    private var isExpandedMars = false
    private var isExpandedEarth = false
    private var isExpandedMarsInfo = false
    private var isExpandedEarthInfo = false

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

        view_pager.adapter = context?.let { ViewPagerAdapter(childFragmentManager, it) }

        tab_layout.setupWithViewPager(view_pager)
        tab_layout.getTabAt(0)?.setIcon(R.drawable.ic_earth)
        tab_layout.getTabAt(1)?.setIcon(R.drawable.ic_planets)

        setCustomTabs()
        setHighlightedTab(EARTH)
        viewPagerPageChangeListener()
    }

    private fun viewPagerPageChangeListener() {
        view_pager.addOnPageChangeListener(object : OnPageChangeListener {

            override fun onPageSelected(position: Int) {
                setHighlightedTab(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float,positionOffsetPixels: Int) {
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
                val url = serverResponseData.photos?.get(0)?.imgSrc
                if (url.isNullOrEmpty()) {
                    toast("Link is empty")
                } else {
                    Log.e("11111111111111111", "renderDataMars url NotNullOrEmpty")
                    circularProgressbar_mars.visibility = View.GONE
                    image_mars_view.visibility = View.VISIBLE

                    motion_layout_mars_alpha.transitionToEnd()

                    image_mars_view.load(url) {
                        lifecycle(this@PlanetFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }

                    val aboutPhoto = serverResponseData.photos[0].earthDate + " " +
                            serverResponseData.photos[0].rover?.name + " " +
                            serverResponseData.photos[0].rover?.status
                    imageMarsClick()
                    isExpandedMarsInfo = false
                    setFABMars(aboutPhoto)
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

    private fun setFABMars(aboutPhoto: String) {
        setInitialStateMars()

        fab_mars.setOnClickListener {
            if (isExpandedMarsInfo) {
                collapseFabMars()
            } else {
                expandFABMars(aboutPhoto)
            }
        }
    }

    private fun setInitialStateMars() {
        image_mars_view.apply {
            alpha = 1f
        }
        mars_option_one_container.apply {
            alpha = 0f
            isClickable = false
        }
    }

    private fun expandFABMars(aboutPhoto: String) {
            isExpandedMarsInfo = true
            ObjectAnimator.ofFloat(mars_option_one_container, "translationY", -100f).start()
            mars_option_one_container.animate()
                .alpha(1f)
                .setDuration(300)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        mars_option_one_container.isClickable = true
                        mars_option_one_container.setOnClickListener {
                            toast(aboutPhoto)
                        }
                    }
                })
        image_mars_view.animate()
            .alpha(0.5f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    image_mars_view.isClickable = false
                }
            })
    }

    private fun collapseFabMars() {
        isExpandedMarsInfo = false
        ObjectAnimator.ofFloat(mars_option_one_container, "translationY", 0f).start()
        mars_option_one_container.animate()
            .alpha(0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    mars_option_one_container.isClickable = false
                }
            })
      image_mars_view.animate()
            .alpha(1.0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    image_mars_view.isClickable = true
                }
            })
    }


    private fun setFABEarth(aboutPhoto: String) {
        setInitialStateEarth()

        fab_earth.setOnClickListener {
            if (isExpandedEarthInfo) {
                collapseFabEarth()
            } else {
                expandFABEarth(aboutPhoto)
            }
        }
    }

    private fun setInitialStateEarth() {
        image_earth_view.apply {
            alpha = 1f
        }
        earth_option_one_container.apply {
            alpha = 0f
            isClickable = false
        }
    }

    private fun expandFABEarth(aboutPhoto: String) {
            isExpandedEarthInfo = true
            ObjectAnimator.ofFloat(earth_option_one_container, "translationY", -100f).start()
            earth_option_one_container.animate()
                .alpha(1f)
                .setDuration(300)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        earth_option_one_container.isClickable = true
                        earth_option_one_container.setOnClickListener {
                            toast(aboutPhoto)
                        }
                    }
                })
        image_earth_view.animate()
            .alpha(0.5f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    image_earth_view.isClickable = false
                }
            })
    }

    private fun collapseFabEarth() {
        isExpandedEarthInfo = false
        ObjectAnimator.ofFloat(earth_option_one_container, "translationY", 0f).start()
        earth_option_one_container.animate()
            .alpha(0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    earth_option_one_container.isClickable = false
                }
            })
      image_earth_view.animate()
            .alpha(1.0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    image_earth_view.isClickable = true
                }
            })
    }

    private fun imageMarsClick() {
        image_mars_view.setOnClickListener {
            isExpandedMars = !isExpandedMars
            TransitionManager.beginDelayedTransition(
                motion_layout_mars_alpha, TransitionSet()
                    .addTransition(ChangeBounds())
                    .addTransition(ChangeImageTransform())
            )

            val params: ViewGroup.LayoutParams = image_mars_view.layoutParams
            params.height =
                if (isExpandedMars) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT
            image_mars_view.layoutParams = params
            image_mars_view.scaleType =
                if (isExpandedMars) ImageView.ScaleType.CENTER_CROP else ImageView.ScaleType.FIT_CENTER
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

                    motion_layout_earth_alpha.transitionToEnd()

                    image_earth_view.load(serverResponseData) {
                        lifecycle(this@PlanetFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }

                    val aboutPhoto = "Earth 2019-05-30"
                    imageEarthClick()
                    isExpandedEarthInfo = false
                    setFABEarth(aboutPhoto)

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

    private fun imageEarthClick() {
        image_earth_view.setOnClickListener {
            isExpandedEarth = !isExpandedEarth
            TransitionManager.beginDelayedTransition(
                motion_layout_earth_alpha, TransitionSet()
                    .addTransition(ChangeBounds())
                    .addTransition(ChangeImageTransform())
            )

            val params: ViewGroup.LayoutParams = image_earth_view.layoutParams
            params.height =
                if (isExpandedEarth) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT
            image_earth_view.layoutParams = params
            image_earth_view.scaleType =
                if (isExpandedEarth) ImageView.ScaleType.CENTER_CROP else ImageView.ScaleType.FIT_CENTER
        }
    }

    private fun Fragment.toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 250)
            show()
        }
    }
}