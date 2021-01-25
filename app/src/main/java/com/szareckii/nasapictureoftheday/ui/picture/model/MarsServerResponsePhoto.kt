package com.szareckii.nasapictureoftheday.ui.picture.model

import com.google.gson.annotations.SerializedName

data class MarsServerResponsePhoto(
    @field:SerializedName("photos") val photos: List<MarsServerResponseData>?
)