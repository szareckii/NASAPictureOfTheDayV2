package com.szareckii.nasapictureoftheday.ui.picture.model

import com.google.gson.annotations.SerializedName

data class MarsServerResponseData(
    @field:SerializedName("id") val id: String?,
    @field:SerializedName("img_src") val imgSrc: String?,
    @field:SerializedName("earth_date") val earthDate: String?,
    @field:SerializedName("rover") val rover: MarsServerResponseRover?
)