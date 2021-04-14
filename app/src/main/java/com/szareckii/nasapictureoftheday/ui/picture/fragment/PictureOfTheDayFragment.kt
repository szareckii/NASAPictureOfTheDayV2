package com.szareckii.nasapictureoftheday.ui.picture.fragment

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import coil.api.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.szareckii.nasapictureoftheday.R
import com.szareckii.nasapictureoftheday.ui.picture.viewModel.day.PictureOfTheDayData
import com.szareckii.nasapictureoftheday.ui.picture.viewModel.day.PictureOfTheDayViewModel
import kotlinx.android.synthetic.main.bottom_sheet_layout.*
import kotlinx.android.synthetic.main.fragment_earth_start.*
import kotlinx.android.synthetic.main.fragment_pod_start.*
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class PictureOfTheDayFragment : Fragment() {

    lateinit var sharedPref: SharedPreferences

    companion object {
        fun newInstance() = PictureOfTheDayFragment()
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProviders.of(this).get(PictureOfTheDayViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getData()
            .observe(viewLifecycleOwner, { renderData(it) })
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_pod_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        input_layout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${input_edit_text.text.toString()}")
            })
        }

        setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet_container))
        setWikiButton()

    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url

                if (url.isNullOrEmpty()) {
                    toast("Link is empty")
                } else {
                    serverResponseData.title.let {
                        setHeaderSpan(it)
                    }

                    serverResponseData.explanation.let {

                        setDescriptionSpan(it)
                    }

                    if (serverResponseData.mediaType == "video") {
                        playVideoType(url)
                    } else {
                        circularProgressbar_POD.visibility = View.GONE
                        third_party_player_view.visibility = View.GONE
                        image_pod_view.visibility = View.VISIBLE
                        image_pod_view.load(url) {
                            lifecycle(this@PictureOfTheDayFragment)
                            error(R.drawable.ic_load_error_vector)
                            placeholder(R.drawable.ic_no_photo_vector)
                        }
                    }
                }
            }
            is PictureOfTheDayData.Loading -> {
                //showLoading()
            }
            is PictureOfTheDayData.Error -> {
                toast(data.error.message)
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun setDescriptionSpan(it: String?) {
        val astronomyTerms = listOf(
                "star",
                "stars",
                "sun",
                "moon",
                "aperture",
                "asterism",
                "asteroid",
                "astronomical unit",
                "averted vision",
                "baily's beads",
                "barlow lens",
                "black hole",
                "blue moon",
                "celestial coordinates",
                "circumpolar",
                "collimation",
                "comet",
                "compound telescope",
                "conjunction",
                "constellation",
                "culmination",
                "dark adaptation",
                "declination",
                "dobsonian",
                "double star",
                "earthshine",
                "eccentricity",
                "eclipse",
                "ecliptic",
                "elongation",
                "ephemeris",
                "equinox",
                "eyepiece",
                "field of view",
                "finderscope",
                "focal length",
                "focal ratio",
                "galaxy",
                "gibbous",
                "histogram",
                "inclination",
                "libration",
                "light pollution",
                "light-year",
                "limb",
                "magnification",
                "magnitude",
                "meridian",
                "messier object",
                "meteor",
                "meteor shower",
                "milky way",
                "mount",
                "nebula",
                "objective",
                "occultation",
                "opposition",
                "parallax",
                "phase",
                "planisphere",
                "reflector",
                "refractor",
                "retrograde",
                "right ascension",
                "seeing",
                "sidereal time",
                "solar filter",
                "solstice",
                "star",
                "star cluster",
                "star diagonal",
                "star party",
                "sunspot",
                "supernova",
                "terminator",
                "transit",
                "transparency",
                "twilight",
                "unit-power fnder",
                "universal time",
                "variable star",
                "waning",
                "waxing",
                "zenith",
                "zodiac"
        )

        val inputStr  = it.toString().toLowerCase(Locale.ROOT)
        val patternStr = Regex("\\b(?:${astronomyTerms.joinToString(separator = "|")})\\b")
        val pattern = Pattern.compile(patternStr.toString())
        val matcher  = pattern.matcher(inputStr)

        val spanText = SpannableStringBuilder(it)

        while (matcher.find()) {

//            spanText.setSpan(
//                ForegroundColorSpan(Color.RED),
//                matcher.start(),
//                matcher.end(),
//                0
//            )

            val clickableSpan = object : ClickableSpan() {
                    override fun onClick(view: View) {
                        Toast.makeText(view.context, "Clicked!", Toast.LENGTH_SHORT).show()
                    }
            }

            spanText.setSpan(
                    clickableSpan,
                    matcher.start(),
                    matcher.end(),
                    0
            )
        }

        bottom_sheet_description.setText(spanText, TextView.BufferType.SPANNABLE)
        bottom_sheet_description.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun setHeaderSpan(it: String?) {
        val spannable = SpannableString(it)
        spannable.setSpan(
                ForegroundColorSpan(Color.BLUE),
                0, spannable.length,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        bottom_sheet_description_header.text = spannable
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun playVideoType(url: String) {
        image_pod_view.visibility = View.GONE
        circularProgressbar_POD.visibility = View.GONE
        third_party_player_view.visibility = View.VISIBLE
        third_party_player_view.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {

                val pattern = "(?<=watch\\?v=|/videos/|embed/|youtu.be/|/v/|/e/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#&?\\n]*"

                val compiledPattern = Pattern.compile(pattern)
                val matcher = compiledPattern.matcher(url)

                if (matcher.find()) {
                    youTubePlayer.cueVideo(matcher.group(), 0f)
                }

            }
        })
    }

    fun setWikiButton() {
        sharedPref = this.activity!!.getSharedPreferences("ui.MainActivity", MODE_PRIVATE)

        when (sharedPref.getInt(getString(R.string.theme), 1)) {
            1 -> wiki_button.setImageResource(R.drawable.ic_wikipedia)
            2 -> wiki_button.setImageResource(R.drawable.ic_wikipedia_grey)
        }
    }

    private fun Fragment.toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 250)
            show()
        }
    }
}