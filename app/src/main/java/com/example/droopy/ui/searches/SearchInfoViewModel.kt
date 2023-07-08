package com.example.droopy.ui.searches

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.droopy.models.SearchInfo
import com.example.droopy.ui.api.ApiService
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.time.LocalDateTime
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
        "res/drawable/manifestacion.jpeg",
        SearchInfo.Consumer(SearchInfo.Company("CNN"))
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
        "res/drawable/utn_frba.jpeg",
        SearchInfo.Consumer(SearchInfo.Company("CNN"))
    )

class SearchInfoViewModel : ViewModel() {
    private val _searchInfoState = MutableStateFlow<SearchInfo?>(null)
    val isLoading = MutableStateFlow(true)
    val error = MutableStateFlow<String?>(null)
    val searchInfoState: StateFlow<SearchInfo?> get() = _searchInfoState

    private val baseUrl = "http://192.168.0.59:3001/api/"

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
                Log.d(this.javaClass.name, "Fetched search info: $search")
                _searchInfoState.value = search
                isLoading.value = false
            } catch (e: Exception) {
                Log.e(
                    this.javaClass.name,
                    "Error fetching search info $searchId with token $token",
                    e
                )
                error.value = e.message
            }
        }
    }
}
