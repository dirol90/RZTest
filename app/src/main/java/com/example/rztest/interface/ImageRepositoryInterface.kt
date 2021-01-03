package com.example.rztest.`interface`

import androidx.lifecycle.MutableLiveData
import com.example.rztest.model.UnsplashPhotoModel

interface ImageRepositoryInterface {

    fun searchWithQuery(
        query: String,
        imageWidth: Int,
        imageHeight: Int,
        buffer: ByteArray?,
        lastPaginationIndex: Int,
        pageLimitValue: Int,
        liveData: MutableLiveData<List<UnsplashPhotoModel?>?>,
        showProgress: MutableLiveData<Boolean>
    ): MutableLiveData<List<UnsplashPhotoModel?>?>

    fun getPopular(
        imageWidth: Int,
        imageHeight: Int,
        buffer: ByteArray?,
        lastPaginationIndex: Int,
        pageLimitValue: Int,
        liveData: MutableLiveData<List<UnsplashPhotoModel?>?>,
        showProgress: MutableLiveData<Boolean>
    ): MutableLiveData<List<UnsplashPhotoModel?>?>

}