package com.szareckii.nasapictureoftheday.ui.picture.model

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IPictureAPI {

    @GET("planetary/apod")
    fun getPictureOfTheDay(@Query("api_key") apiKey: String): Call<PODServerResponseData>

    @GET("mars-photos/api/v1/rovers/curiosity/photos?sol=1000&camera=fhaz")
    fun getPictureOfTheMars(@Query("api_key") apiKey: String): Call<MarsServerResponsePhoto>

    @GET("EPIC/archive/natural/2019/05/30/png/epic_1b_20190530011359.png")
    fun getPictureEPIC(@Query("api_key") apiKey: String): Call<ResponseBody>
}