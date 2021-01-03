package com.example.rztest.`interface`

interface MainViewModelInterface {

    fun searchWithQuery(query: String, imageWidth: Int, imageHeight: Int, buffer : ByteArray?)
    fun getPopular(imageWidth: Int, imageHeight: Int, buffer : ByteArray?)
    fun scrollViewer(visibleItemCount : Int, firstVisibleItemPosition : Int, totalItemCount : Int, text : String, imageWidth : Int, imageHeight : Int, buffer : ByteArray?)
    fun cleanPaginationIndex()
    fun updateIsLoadingStatus(isLoading: Boolean)

}