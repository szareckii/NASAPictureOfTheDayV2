package com.szareckii.nasapictureoftheday.ui.picture.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import coil.api.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.szareckii.nasapictureoftheday.R
import com.szareckii.nasapictureoftheday.ui.picture.viewmodel.day.PictureOfTheDayData
import com.szareckii.nasapictureoftheday.ui.picture.viewmodel.day.PictureOfTheDayViewModel
import kotlinx.android.synthetic.main.bottom_sheet_layout.*
import kotlinx.android.synthetic.main.fragment_pictureoftheday.*

class PictureOfTheDayFragment : Fragment() {

    companion object {
        fun newInstance() = PictureOfTheDayFragment()
        private var isMain = true
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    //Ленивая инициализация модели
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
        return inflater.inflate(R.layout.fragment_pictureoftheday, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        input_layout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${input_edit_text.text.toString()}")
            })
        }

        setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet_container))
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
                        bottom_sheet_description_header.text = it
                    }

                    serverResponseData.explanation.let {
                        bottom_sheet_description.text = it
                    }

                    if (serverResponseData.mediaType == "video") {
                        image_view.visibility = View.GONE
                        circularProgressbar_POD.visibility = View.GONE
                        webView.visibility = View.VISIBLE
                        webView.clearCache(true)
                        webView.clearHistory()
                        webView.settings.javaScriptEnabled = true
                        webView.settings.javaScriptCanOpenWindowsAutomatically = true
                        webView.loadUrl(url)
                    } else {
                        circularProgressbar_POD.visibility = View.GONE
                        webView.visibility = View.GONE
                        image_view.visibility = View.VISIBLE
                        image_view.load(url) {
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

    private fun Fragment.toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 250)
            show()
        }
    }

}