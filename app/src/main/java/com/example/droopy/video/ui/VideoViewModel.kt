
package com.example.droopy.video.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay

class VideoViewModel : ViewModel() {
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

    suspend fun onVideoInitialized(filmPostulationId: String) {
        _isLoading.value = true
        delay(4000)
        val filmSearchSelected = 1

        // GET TO film_postulation/accepted?filmSearch=${filmSearchUuid}
        // That endpoint will return the filmPostulationId that will be the channelName and will be used in below endpoint

        //POST TO film_postulation/filmPostulationId/video-call with bearer token of logged user
        // That endpoint will return the videoToken and the chatToken.
        
        onFetchedToken("006a997ba8743d44cf8bc3bec156c7fe7f1IADvXWE43kpPN6ugi9LooO2QVbbC/w6ecnFfLXk3W/p7lDgbtvO379yDIgA8qo3ji3iQZAQAAQADtUlvAgADtUlvAwADtUlvBAADtUlv" ,"006a997ba8743d44cf8bc3bec156c7fe7f1IAAPLRx0/fJ1W5t0EdnuMRSscQctldv4T9Q47oIWvrPBjrfv3IMAAAAAEAA8qo3ji3iQZAEA6AMDtUlv" ,"4")

       // Pendiente

        //1. Pegarle a la api para obtener el video tokenel, chat token y el postulationId y usarlo (para esto se debe mergear con el login para obtener el auth token y el user id logeado si o si )
        //2. Estilizar chat module para ir mostrando los mensajes que van llegando
        //3. Finalizar llamada (pegarle a la api al /finish y volver a la pantalla del mapa que debera recargas las busquedas supuestamete)

        _isLoading.value = false
    }

    suspend fun onFinish(filmSearchId: String){
        //POST to film_search/filmSearchId/finish
        _isLoading.value = true
        delay(4000)
        _isLoading.value = false
    }
}