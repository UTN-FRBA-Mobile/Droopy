package com.example.droopy.ui.searches

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.droopy.models.SearchInfo
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import com.example.droopy.R
import com.example.droopy.models.SearchStatus
import com.example.droopy.ui.api.ApiService
import com.google.gson.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.time.format.DateTimeFormatter

val searchInfoMock =
    SearchInfo(
        "searchinfo1234",
        "Corte 9 Julio",
        "Queremos capturar la concentración de manifestantes que se extienden desde la subida a la autopista en 9 de Julio y San Juan, hasta el Obelisco.",
        1,
        LocalDateTime.now(),
        LocalDateTime.now(),
        isPaid = true,
        "res/drawable/manifestacion.jpeg"
    )

val searchInfoMock2 =
    SearchInfo(
        "searchInfoUtnTestt",
        "UTN",
        "Incidentes en la recibida de Ingenieros de la UTN",
        1,
        LocalDateTime.now(),
        LocalDateTime.now(),
        isPaid = true,
        "res/drawable/utn_frba.jpeg"
    )

class SearchInfoViewModel : ViewModel() {
    private val _searchInfoState = MutableStateFlow<SearchInfo?>(null)
    val searchInfoState: StateFlow<SearchInfo?> get() = _searchInfoState

    private val baseUrl = "http://172.16.0.22:3001/"

    val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, object : JsonDeserializer<LocalDateTime> {
            override fun deserialize(
                json: JsonElement?,
                typeOfT: Type?,
                context: JsonDeserializationContext?
            ): LocalDateTime {
                return LocalDateTime.parse(json?.asString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            }
        })
        .create()

    private val service: ApiService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build().create(ApiService::class.java)

    fun getSearchInfoById(searchId: String, token: String) {
        viewModelScope.launch {
            try {
                val search = service.getFilmSearchById(searchId, "Bearer $token")
                _searchInfoState.value = search
            } catch (e: Exception) {
                Log.e(this.javaClass.name, "Error fetching search info $searchId with token $token", e)
            }
        }
    }

    // TODO: Move this to a repository class
    private suspend fun fetchSearchById(searchId: String): SearchInfo {
        if(searchId == "1") {
            return searchInfoMock
        }
        return searchInfoMock2
    }
}
