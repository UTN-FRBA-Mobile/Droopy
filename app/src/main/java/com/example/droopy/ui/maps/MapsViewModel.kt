package com.example.droopy.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.droopy.models.FilmSearchesResponse
import com.example.droopy.ui.api.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MapsViewModel : ViewModel() {
    private val baseUrl = "http://192.168.0.142:3001/"

    private val _filmSearchesResponse = MutableLiveData<List<FilmSearchesResponse>>()
    val filmSearchesResponse: LiveData<List<FilmSearchesResponse>> = _filmSearchesResponse

    fun fetchFilmSearchConsumer(token: String) {
        viewModelScope.launch {
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(ApiService::class.java)

            val limit = 10
            val offset = 0

            try {
                val response = apiService.getFilmSearchConsumer(limit, offset, "Bearer $token")
                _filmSearchesResponse.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
