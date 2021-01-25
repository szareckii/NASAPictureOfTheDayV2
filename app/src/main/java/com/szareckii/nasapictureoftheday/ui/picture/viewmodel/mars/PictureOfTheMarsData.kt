package com.szareckii.nasapictureoftheday.ui.picture.viewmodel.mars

import com.szareckii.nasapictureoftheday.ui.picture.model.MarsServerResponsePhoto

sealed class PictureOfTheMarsData {
    data class Success(val serverResponseData: MarsServerResponsePhoto) : PictureOfTheMarsData()
    data class Error(val error: Throwable) : PictureOfTheMarsData()
    data class Loading(val progress: Int?) : PictureOfTheMarsData()
}