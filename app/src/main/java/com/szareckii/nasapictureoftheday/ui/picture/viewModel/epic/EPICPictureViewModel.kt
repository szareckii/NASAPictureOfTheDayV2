package com.szareckii.nasapictureoftheday.ui.picture.viewModel.epic

import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.szareckii.nasapictureoftheday.BuildConfig
import com.szareckii.nasapictureoftheday.ui.picture.model.PicturesRetrofitImpl
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EPICPictureViewModel(
        private val liveDataForViewToObserve: MutableLiveData<EPICPictureData> = MutableLiveData(),
        private val retrofitImpl: PicturesRetrofitImpl = PicturesRetrofitImpl()
) :
    ViewModel() {

    fun getData(): LiveData<EPICPictureData> {
        sendServerRequest()
        return liveDataForViewToObserve
    }

    private fun sendServerRequest() {
        liveDataForViewToObserve.value = EPICPictureData.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            EPICPictureData.Error(Throwable("You need API key"))
        } else {
            Log.e("11111111111111111", " retrofitImpl.getRetrofitImpl().getPictureEPIC(apiKey")
            retrofitImpl.getRetrofitImpl().getPictureEPIC(apiKey).enqueue(object :
                    Callback<ResponseBody> {
                override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful && response.body() != null) {

                        val bmp = BitmapFactory.decodeStream(response.body()!!.byteStream())

                        liveDataForViewToObserve.value =
                                EPICPictureData.Success(bmp!!)

                        Log.e("11111111111111111", "EPICPictureData.Success")

                    } else {
                        val message = response.message()
                        if (message.isNullOrEmpty()) {
                            liveDataForViewToObserve.value =
                                    EPICPictureData.Error(Throwable("Unidentified error"))
                        } else {
                            liveDataForViewToObserve.value =
                                    EPICPictureData.Error(Throwable(message))
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    liveDataForViewToObserve.value = EPICPictureData.Error(t)
                }
            })
        }
    }
}