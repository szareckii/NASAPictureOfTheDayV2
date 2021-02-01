package com.szareckii.nasapictureoftheday.ui.picture.model

import com.google.gson.annotations.SerializedName

data class MarsServerResponseRover(
        @field:SerializedName("id") val id: String?,
        @field:SerializedName("name") val name: String?,
        @field:SerializedName("status") val status: String?
)