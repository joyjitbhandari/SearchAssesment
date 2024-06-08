package com.app.searchassesment.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.searchassesment.repository.Repository
import com.app.searchassesment.viewModel.SearchViewModel

class ViewModelFactory(private val repository: Repository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T:ViewModel> create(modelClass:Class<T>):T{
        return when {
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> SearchViewModel(repository) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}