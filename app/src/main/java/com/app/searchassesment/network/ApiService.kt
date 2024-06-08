package com.app.searchassesment.network

import com.app.searchassesment.model.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/")
    suspend fun getSearchData(
        @Query("type") type:String = null ?: "movie",
        @Query("apikey") key:String = null ?: "5d81e1ce",
        @Query("page") page:String = null ?: "1",
        @Query("s") search:String = null ?: "abc" ,
    ): Response<SearchResponse>
}