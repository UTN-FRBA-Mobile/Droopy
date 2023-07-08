package com.example.droopy.video

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.droopy.ui.theme.DroopyTheme
import com.example.droopy.video.ui.VideoScreen
import com.example.droopy.video.ui.VideoViewModel

class VideoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "")
        val selectedFilmSearchId = sharedPreferences.getString("selectedFilmSearchId", "")

        setContent {
            DroopyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                )
                {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "video"
                    ) {
                        composable("video") {
                            VideoScreen(VideoViewModel(), selectedFilmSearchId?:"" ,token ?: "")
                        }
                    }
                }
            }
        }
    }
}
