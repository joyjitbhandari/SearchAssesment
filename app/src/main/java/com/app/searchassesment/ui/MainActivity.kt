package com.app.searchassesment.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.app.searchassesment.adapter.PaginationAdapter
import com.app.searchassesment.databinding.ActivityMainBinding
import com.app.searchassesment.model.Search
import com.app.searchassesment.network.NetworkResponse
import com.app.searchassesment.network.RetrofitClient
import com.app.searchassesment.repository.Repository
import com.app.searchassesment.utill.InternetConnection
import com.app.searchassesment.viewModel.SearchViewModel
import com.app.searchassesment.viewModel.factory.ViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: SearchViewModel
    private var pageCount = 1
    private var resultList = arrayListOf<Search>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(Repository(RetrofitClient.apiService))
        )[SearchViewModel::class.java]

        initObserver()

        perFormSearch(pageCount.toString(), "movie")

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                pageCount = 1
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.searchView.windowToken, 0)
                if (query!!.isNotEmpty()) {
                    perFormSearch(pageCount.toString(), query.toString())
                } else Toast.makeText(this@MainActivity, "Search is empty", Toast.LENGTH_SHORT)
                    .show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                pageCount = 1
                if (newText!!.isNotEmpty() && newText.length >= 3) {
                    perFormSearch(pageCount.toString(), newText)
                }
                return true
            }
        })
        binding.searchView.requestFocus()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserver() {
        viewModel.searchResponse.observe(this as LifecycleOwner) {
            when (it) {
                is NetworkResponse.Loading -> {
                    binding.progressBar.visibility = android.view.View.VISIBLE
                }

                is NetworkResponse.Success -> {
                    binding.progressBar.visibility = android.view.View.GONE
                    if (it.data?.Search?.isNotEmpty() == true) {
                        if (pageCount == 1) {
                            resultList.clear()
                            resultList.addAll(it.data.Search)
                            binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
                            binding.recyclerView.adapter = PaginationAdapter(resultList) {
                                pageCount += 1
                                perFormSearch(
                                    pageCount.toString(),
                                    binding.searchView.query.toString()
                                )
                            }
                        }else{
                            resultList.addAll(it.data.Search)
                            binding.recyclerView.adapter?.notifyDataSetChanged()
                        }

                    } else Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show()
                }

                is NetworkResponse.Error -> {
                    binding.progressBar.visibility = android.view.View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun perFormSearch(page: String, search: String) {
        lifecycleScope.launch {
            if (InternetConnection.isNetworkAvailable(this@MainActivity)) {
                viewModel.getSearchData(page, search)
            } else Toast.makeText(this@MainActivity, "No internet connection", Toast.LENGTH_SHORT)
                .show()
        }
    }
}