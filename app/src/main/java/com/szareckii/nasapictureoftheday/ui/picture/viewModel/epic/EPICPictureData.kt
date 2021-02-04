package com.szareckii.nasapictureoftheday.ui.picture.viewModel.epic

import android.graphics.Bitmap

sealed class EPICPictureData {
    data class Success(val serverResponseData: Bitmap) : EPICPictureData()
    data class Error(val error: Throwable) : EPICPictureData()
    data class Loading(val progress: Int?) : EPICPictureData()
}
