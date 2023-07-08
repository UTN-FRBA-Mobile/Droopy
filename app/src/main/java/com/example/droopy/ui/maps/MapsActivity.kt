package com.example.droopy.ui.maps

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.droopy.ui.maps.ui.theme.DroopyTheme
import com.example.droopy.ui.searches.SearchInfoScreen
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker

class MapsActivity : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var mapsViewModel: MapsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapsViewModel = ViewModelProvider(this)[MapsViewModel::class.java]
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "")
        setContent {
            DroopyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "map"
                    ) {
                        composable("map") {
                            if (token != null) {
                                MyGoogleMaps(navController, mapsViewModel, token)
                            }
                        }
                        composable(
                            "searchInfo/{searchId}",
                            arguments = listOf(
                                navArgument(
                                    "searchId"
                                ) { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            SearchInfoScreen(searchId = backStackEntry.arguments?.getString("searchId")!!)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MyGoogleMaps(navController: NavHostController, mapsViewModel: MapsViewModel, token: String) {
    val lat = remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        mapsViewModel.fetchFilmSearchConsumer(token)
    }
    val filmSearchesResponse by mapsViewModel.filmSearches.observeAsState()
    filmSearchesResponse?.let {
        lat.value = it.getOrNull(0)?.location?.latitude
    }

    // todo: use device location
    val cameraPosition = CameraPosition(
        LatLng(-34.598665, -58.420281),
        12f,
        0f,
        0f
    )
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = CameraPositionState(cameraPosition)
    ) {
        filmSearchesResponse?.forEach { filmSearch ->
            val latitude = filmSearch.location?.latitude?.toDoubleOrNull()
            val longitude = filmSearch.location?.longitude?.toDoubleOrNull()
            if (latitude != null && longitude != null) {
                val markerPosition = LatLng(latitude, longitude)
                val markerTitle = filmSearch.location.address ?: ""
                Marker(position = markerPosition, title = markerTitle, onClick = {
                    navController.navigate("searchInfo/${filmSearch.uuid}")
                    true
                })
            }
        }
    }
}
