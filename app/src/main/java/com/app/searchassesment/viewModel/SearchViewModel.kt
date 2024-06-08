package com.app.searchassesment.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.searchassesment.model.SearchResponse
import com.app.searchassesment.network.NetworkResponse
import com.app.searchassesment.repository.Repository
import kotlinx.coroutines.launch
import org.json.JSONObject

class SearchViewModel(private val repository: Repository) : ViewModel() {
    val TAG = "response"

    private val searchResponseLiveData =
        MutableLiveData<NetworkResponse<SearchResponse>>()
    val searchResponse get() = searchResponseLiveData


    fun getSearchData(page: String, search: String) = viewModelScope.launch {
        searchResponseLiveData.value = NetworkResponse.Loading()
        try {
            val response = repository.getSearchData(page, search)
            if (response.isSuccessful) {
                searchResponseLiveData.value = NetworkResponse.Success(response.body()!!)
            } else {
                val errorObject = JSONObject(response.errorBody()!!.string())
                val errorData = errorObject.getJSONObject("error")
                searchResponseLiveData.value = NetworkResponse.Error(errorData.getString("message"))
            }

        } catch (e: Exception) {
            searchResponseLiveData.value = NetworkResponse.Error("Something went wrong")
            Log.d(TAG, "getSearchData: ${e.message}")
        }
    }

}