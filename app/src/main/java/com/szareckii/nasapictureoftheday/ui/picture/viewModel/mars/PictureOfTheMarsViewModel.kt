package com.szareckii.nasapictureoftheday.ui.picture.viewModel.mars

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.szareckii.nasapictureoftheday.BuildConfig
import com.szareckii.nasapictureoftheday.ui.picture.model.MarsServerResponsePhoto
import com.szareckii.nasapictureoftheday.ui.picture.model.PicturesRetrofitImpl
import com.szareckii.nasapictureoftheday.ui.picture.viewModel.day.PictureOfTheDayData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PictureOfTheMarsViewModel(
    private val liveDataForViewToObserve: MutableLiveData<PictureOfTheMarsData> = MutableLiveData(),
    private val retrofitImpl: PicturesRetrofitImpl = PicturesRetrofitImpl()
) :
    ViewModel() {

    fun getData(): LiveData<PictureOfTheMarsData> {
        sendServerRequest()
        return liveDataForViewToObserve
    }

    private fun sendServerRequest() {
        liveDataForViewToObserve.value = PictureOfTheMarsData.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            PictureOfTheDayData.Error(Throwable("You need API key"))
        } else {
            retrofitImpl.getRetrofitImpl().getPictureOfTheMars(apiKey).enqueue(object :
                Callback<MarsServerResponsePhoto> {
                override fun onResponse(
                    call: Call<MarsServerResponsePhoto>,
                    response: Response<MarsServerResponsePhoto>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        liveDataForViewToObserve.value =
                            PictureOfTheMarsData.Success(response.body()!!)
                    } else {
                        val message = response.message()
                        if (message.isNullOrEmpty()) {
                            liveDataForViewToObserve.value =
                                PictureOfTheMarsData.Error(Throwable("Unidentified error"))
                        } else {
                            liveDataForViewToObserve.value =
                                PictureOfTheMarsData.Error(Throwable(message))
                        }
                    }
                }

                override fun onFailure(call: Call<MarsServerResponsePhoto>, t: Throwable) {
                    liveDataForViewToObserve.value = PictureOfTheMarsData.Error(t)
                }
            })
        }
    }


}