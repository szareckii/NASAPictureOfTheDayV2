package com.szareckii.nasapictureoftheday.ui.picture.model

import com.google.gson.annotations.SerializedName

data class MarsServerResponseData(
    @field:SerializedName("id") val id: String?,
    @field:SerializedName("img_src") val img_src: String?
)