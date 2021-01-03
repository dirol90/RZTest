package com.example.rztest.repository

import androidx.annotation.NonNull
import androidx.lifecycle.MutableLiveData
import com.example.rztest.`interface`.ImageRepositoryInterface
import com.example.rztest.`interface`.UnsplashApiInterface
import com.example.rztest.model.SearchResultModel
import com.example.rztest.model.UnsplashPhotoModel
import com.example.rztest.service.ApiService
import com.example.rztest.utils.EncoderUtil
import okhttp3.internal.notify
import okhttp3.internal.notifyAll
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImageRepository : ImageRepositoryInterface {

    companion object {
        private var imageRepository: ImageRepository? = null
        private var unsplashApiInterface: UnsplashApiInterface? = null
        private fun repository() {
            unsplashApiInterface = ApiService.createService(UnsplashApiInterface::class.java)
        }

        fun getInstance(): ImageRepository? {
            if (imageRepository == null) {
                imageRepository = ImageRepository()
                repository()
            }
            return imageRepository
        }
    }

    override fun searchWithQuery(
        query: String,
        imageWidth: Int,
        imageHeight: Int,
        buffer: ByteArray?,
        lastPaginationIndex: Int,
        pageLimitValue: Int,
        liveData: MutableLiveData<List<UnsplashPhotoModel?>?>,
        showProgress: MutableLiveData<Boolean>
    ): MutableLiveData<List<UnsplashPhotoModel?>?> {

        if (query.isNotEmpty()) {

            showProgress.postValue(true)

            unsplashApiInterface?.getPhotosWithQuery(
                EncoderUtil.fromFile(buffer!!),
                query,
                lastPaginationIndex,
                pageLimitValue,
                "latest"
            )?.enqueue(
                object : Callback<SearchResultModel?> {
                    override fun onResponse(
                        @NonNull call: Call<SearchResultModel?>?,
                        @NonNull response: Response<SearchResultModel?>?
                    ) {
                        response?.let {
                            if (it.isSuccessful) {
                                liveData.postValue(it.body()!!.results)
                            }
                        }
                        showProgress.postValue(false)
                    }

                    override fun onFailure(
                        @NonNull call: Call<SearchResultModel?>?,
                        @NonNull t: Throwable
                    ) {
                        t.printStackTrace()
                        showProgress.postValue(false)
                    }
                }
            )
        } else {
            getPopular(
                imageWidth,
                imageHeight,
                buffer,
                lastPaginationIndex,
                pageLimitValue,
                liveData,
                showProgress
            )
        }
        return liveData
    }

    override fun getPopular(
        imageWidth: Int,
        imageHeight: Int,
        buffer: ByteArray?,
        lastPaginationIndex: Int,
        pageLimitValue: Int,
        liveData: MutableLiveData<List<UnsplashPhotoModel?>?>,
        showProgress: MutableLiveData<Boolean>
    ): MutableLiveData<List<UnsplashPhotoModel?>?> {

        showProgress.postValue(true)

        unsplashApiInterface?.getPhotos(
            EncoderUtil.fromFile(buffer!!),
            lastPaginationIndex,
            pageLimitValue,
            "latest"
        )?.enqueue(
            object : Callback<List<UnsplashPhotoModel?>?> {
                override fun onResponse(
                    @NonNull call: Call<List<UnsplashPhotoModel?>?>?,
                    @NonNull response: Response<List<UnsplashPhotoModel?>?>?
                ) {
                    response?.let {
                        if (it.isSuccessful) {
                            liveData.postValue(it.body()!!)
                        }
                    }
                    showProgress.postValue(false)
                }

                override fun onFailure(
                    @NonNull call: Call<List<UnsplashPhotoModel?>?>?,
                    @NonNull t: Throwable
                ) {
                    t.printStackTrace()
                    showProgress.postValue(false)
                }
            }
        )
        return liveData
    }

}