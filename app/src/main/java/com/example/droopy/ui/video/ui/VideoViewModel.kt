
package com.example.droopy.video.ui

import android.content.Context
import android.content.Intent
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.droopy.ui.api.ApiService
import com.example.droopy.ui.maps.MapsActivity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VideoViewModel : ViewModel() {
    private val baseUrl = "http://192.168.0.59:3001/api/"

    private val _videoToken = MutableLiveData<String>()
    val videoToken: LiveData<String> = _videoToken

    private val _channel = MutableLiveData<String>()
    private val _chatToken = MutableLiveData<String>()

    val chatToken: LiveData<String> = _chatToken
    val channel: LiveData<String> = _channel

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private fun onFetchedToken(videoToken: String, chatToken: String, channel: String) {
        _videoToken.value =videoToken
        _chatToken.value=chatToken
        _channel.value = channel
    }

    suspend fun onVideoInitialized(filmSearchId: String, token: String) {
        _isLoading.value = true
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        // That endpoint will return the filmPostulationId that will be the channelName and will be used in below endpoint
        val filmPostulation = apiService.getFilmPostulation(filmSearchId,"Bearer $token")

        // That endpoint will return the videoToken and the chatToken.
        val tokens = apiService.getVideoAndChatTokens(filmPostulation.uuid,"Bearer $token")
        onFetchedToken(tokens.videoToken, tokens.chatToken, filmPostulation.uuid)
        _isLoading.value = false
    }

    suspend fun onFinish(token: String, context: Context){
        _isLoading.value = true
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(ApiService::class.java)
        apiService.finishCall(channel.value?: "", token)

        val intent = Intent(context, MapsActivity::class.java)
        context.startActivity(intent)

        _isLoading.value = false
    }
}
