
package com.example.droopy.video.ui

import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.droopy.ui.api.ApiService
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

        // GET TO film_postulation/accepted?filmSearch=${filmSearchId}
        // That endpoint will return the filmPostulationId that will be the channelName and will be used in below endpoint
        val filmPostulation = apiService.getFilmPostulation(filmSearchId,"Bearer $token")

        //POST TO film_postulation/filmPostulationId/video-call with bearer token of logged user
        // That endpoint will return the videoToken and the chatToken.
        val tokens = apiService.getVideoAndChatTokens(filmPostulation.filmPostulationId,"Bearer $token")
        onFetchedToken(tokens.videoToken, tokens.chatToken, filmPostulation.filmPostulationId)

        //2. Estilizar chat module para ir mostrando los mensajes que van llegando
        //3. Finalizar llamada (pegarle a la api al /finish y volver a la pantalla del mapa que debera recargas las busquedas supuestamete)
        _isLoading.value = false
    }

    suspend fun onFinish(token: String){
        //POST to film_search/filmSearchId/finish
        _isLoading.value = true
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(ApiService::class.java)
        apiService.finishCall(channel.value?: "", token)
        _isLoading.value = false
    }
}
