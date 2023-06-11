
package com.example.droopy.video.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay

class VideoViewModel : ViewModel() {
    private val _token = MutableLiveData<String>()
    val token: LiveData<String> = _token
    private val _channel = MutableLiveData<String>()
    val channel: LiveData<String> = _channel
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private fun onFetchedToken(token: String, channel: String) {
        _token.value = token
        _channel.value = channel
    }

    suspend fun onVideoInitialized(filmPostulationId: String) {
        _isLoading.value = true
        delay(4000)
        //POST TO film_postulation/filmPostulationId/video-call
        onFetchedToken("007eJxTYHD+1tP/Y39Dp/C1J9Eugc0Z4b2ap/hO1TR4Fq2NPS3evVmBIdHS0jwp0cLcxDjFxCQ5zSIp2TgpNdnQ1CzZPC3VPM0w1rYtpSGQkSFl3RZGRgYIBPHZGLLzS3Iy8xgYAOmjIXg=", "kotlin")
        _isLoading.value = false
    }

    suspend fun onFinish(filmSearchId: String){
        //POST to film_search/filmSearchId/finish
        _isLoading.value = true
        delay(4000)
        _isLoading.value = false
    }
}