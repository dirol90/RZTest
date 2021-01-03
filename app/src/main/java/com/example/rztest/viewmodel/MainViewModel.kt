package com.example.rztest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rztest.`interface`.MainViewModelInterface
import com.example.rztest.model.UnsplashPhotoModel
import com.example.rztest.repository.ImageRepository

class MainViewModel : ViewModel(), MainViewModelInterface {
    private val pageLimitValue = 10
    private var lastPaginationIndex = 1
    private val imageRepository by lazy {
        ImageRepository.getInstance()
    }

    private val showProgress: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private var data: MutableLiveData<List<UnsplashPhotoModel?>?> =
        MutableLiveData<List<UnsplashPhotoModel?>?>()

    override fun searchWithQuery(
        query: String,
        imageWidth: Int,
        imageHeight: Int,
        buffer: ByteArray?
    ) {
        data = imageRepository?.searchWithQuery(
            query,
            imageWidth,
            imageHeight,
            buffer,
            lastPaginationIndex,
            pageLimitValue,
            data,
            showProgress
        )!!
    }

    override fun getPopular(imageWidth: Int, imageHeight: Int, buffer: ByteArray?) {
        data = imageRepository?.getPopular(
            imageWidth,
            imageHeight,
            buffer,
            lastPaginationIndex,
            pageLimitValue,
            data,
            showProgress
        )!!
    }

    override fun scrollViewer(
        visibleItemCount: Int,
        firstVisibleItemPosition: Int,
        totalItemCount: Int,
        text: String,
        imageWidth: Int,
        imageHeight: Int,
        buffer: ByteArray?
    ) {
        if (!showProgress.value!!) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= pageLimitValue) {
                if (text.isNotEmpty()) {
                    lastPaginationIndex++
                    searchWithQuery(text, imageWidth, imageHeight, buffer)
                } else {
                    lastPaginationIndex++
                    getPopular(imageWidth, imageHeight, buffer)
                }
            }
        }
    }

    fun getData(): LiveData<List<UnsplashPhotoModel?>?> {
        return data
    }

    fun getShowingProgress(): MutableLiveData<Boolean> {
        return showProgress
    }

    override fun cleanPaginationIndex() {
        lastPaginationIndex = 1
    }

    override fun updateIsLoadingStatus(isLoading: Boolean) {
        showProgress.postValue(isLoading)
    }

}