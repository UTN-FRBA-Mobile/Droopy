package com.example.droopy.ui.searches

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.droopy.models.SearchInfo
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.util.Date

val searchInfoMock =
    SearchInfo(
        "searchinfo1234",
        "Corte 9 Julio",
        "Queremos capturar la concentración de manifestantes que se extienden desde la subida a la autopista en 9 de Julio y San Juan, hasta el Obelisco.",
        "CREATED",
        LocalDateTime.now(),
        true
    )

val searchInfoMock2 =
    SearchInfo(
        "searchInfoUtnTestt",
        "UTN",
        "Incidentes en la recibida de Ingenieros de la UTN",
        "CREATED",
        LocalDateTime.now(),
        true
    )

class SearchInfoViewModel : ViewModel() {
    private val _searchInfoState = mutableStateOf<SearchInfo?>(null)
    val searchInfoState: State<SearchInfo?> = _searchInfoState

    fun getSearchInfoById(searchId: String) {
        viewModelScope.launch {
            try {
                val search = fetchSearchById(searchId)
                _searchInfoState.value = search
            } catch (e: Exception) {
                Log.e(this.javaClass.name, "Error fetching search info $searchId")
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
