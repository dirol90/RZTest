package com.example.rztest.`interface`

import com.example.rztest.model.SearchResultModel
import com.example.rztest.model.UnsplashPhotoModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface UnsplashApiInterface {

    @Headers("Accept-Version: v1")
    @GET("photos")
    fun getPhotos(
        @Query("client_id") apiKey : String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("order_by") orderBy: String)
    : Call<List<UnsplashPhotoModel?>?>?

    @Headers("Accept-Version: v1")
    @GET("search/photos")
    fun getPhotosWithQuery(
        @Query("client_id") apiKey : String,
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("order_by") orderBy: String)
            : Call<SearchResultModel?>?

}