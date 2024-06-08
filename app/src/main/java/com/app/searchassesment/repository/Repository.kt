package com.app.searchassesment.repository

import com.app.searchassesment.network.ApiService
import com.app.searchassesment.model.SearchResponse
import com.app.searchassesment.utill.Constance.KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class Repository(private val apiService: ApiService) {
    suspend fun getSearchData(page:String, search:String): Response<SearchResponse> {
        return withContext(Dispatchers.IO){ apiService.getSearchData(page = page, search = search, key = KEY)}
    }
}