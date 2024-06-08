package com.app.searchassesment.model

data class SearchResponse(
    val Response: String,
    val Search: List<Search>,
    val totalResults: String
)