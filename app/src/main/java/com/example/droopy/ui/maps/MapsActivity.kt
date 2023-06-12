package com.example.droopy.ui.maps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.droopy.ui.maps.ui.theme.DroopyTheme
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.droopy.ui.searches.SearchInfoScreen
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.CameraPositionState

class MapsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                            MyGoogleMaps(navController)
                        }
                        composable(
                            "searchInfo/{searchId}",
                            arguments = listOf(
                                navArgument(
                                    "searchId",
                                    { type = NavType.StringType })
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
fun MyGoogleMaps(navController: NavHostController) {
    val markerUtn = LatLng(-34.598665, -58.420281)
    val markerObelisco = LatLng(-34.603766, -58.382443)
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
        Marker(
            position = markerUtn,
            title = "UTN",
            onClick = {
                navController.navigate("searchInfo/2")
                true
            }
        )
        Marker(
            position = markerObelisco,
            title = "Obelisco",
            onClick = {
                val searchId = "test"
                navController.navigate("searchInfo/1")
                true
            }
        )
    }
}
