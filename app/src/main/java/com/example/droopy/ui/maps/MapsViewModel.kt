package com.example.droopy.ui.maps

import android.service.controls.ControlsProviderService
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.droopy.models.FilmSearch
import com.example.droopy.ui.api.ApiService
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MapsViewModel : ViewModel() {
    private val baseUrl = "http://172.16.0.22:3001/"

    private val _filmSearches = MutableLiveData<List<FilmSearch>>()
    val filmSearches: LiveData<List<FilmSearch>> = _filmSearches

    fun fetchFilmSearchConsumer(token: String) {
        viewModelScope.launch {
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(ApiService::class.java)

            try {
                val response = apiService.getSearches("Bearer $token")
                _filmSearches.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
