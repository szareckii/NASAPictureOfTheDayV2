package com.szareckii.nasapictureoftheday.ui.picture.viewModel.day

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.szareckii.nasapictureoftheday.BuildConfig
import com.szareckii.nasapictureoftheday.ui.picture.model.PicturesRetrofitImpl
import com.szareckii.nasapictureoftheday.ui.picture.model.PODServerResponseData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PictureOfTheDayViewModel(
    private val liveDataForViewToObserve: MutableLiveData<PictureOfTheDayData> = MutableLiveData(),
    private val retrofitImpl: PicturesRetrofitImpl = PicturesRetrofitImpl()
) :
    ViewModel() {

    fun getData(): LiveData<PictureOfTheDayData> {
        sendServerRequest()
        return liveDataForViewToObserve
    }

    private fun sendServerRequest() {
        liveDataForViewToObserve.value = PictureOfTheDayData.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            PictureOfTheDayData.Error(Throwable("You need API key"))
        } else {
            retrofitImpl.getRetrofitImpl().getPictureOfTheDay(apiKey).enqueue(object :
                Callback<PODServerResponseData> {
                override fun onResponse(
                    call: Call<PODServerResponseData>,
                    response: Response<PODServerResponseData>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        liveDataForViewToObserve.value =
                            PictureOfTheDayData.Success(response.body()!!)
                    } else {
                        val message = response.message()
                        if (message.isNullOrEmpty()) {
                            liveDataForViewToObserve.value =
                                PictureOfTheDayData.Error(Throwable("Unidentified error"))
                        } else {
                            liveDataForViewToObserve.value =
                                PictureOfTheDayData.Error(Throwable(message))
                        }
                    }
                }

                override fun onFailure(call: Call<PODServerResponseData>, t: Throwable) {
                    liveDataForViewToObserve.value = PictureOfTheDayData.Error(t)
                }
            })
        }
    }
}