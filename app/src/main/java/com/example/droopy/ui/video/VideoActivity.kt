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
import com.example.droopy.ui.theme.DroopyTheme
import com.example.droopy.video.ui.VideoScreen
import com.example.droopy.video.ui.VideoViewModel

class VideoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        // val token = sharedPreferences.getString("token", "")
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImZpbG1tYWtlckBkcm9vcHkuY29tIiwiaWF0IjoxNjg4ODI4MDE0LCJleHAiOjE2ODg4MzE2MTR9.KmOPjAM-u6pZndAqq33WUzQPVizwfdPjy-lwEDRc3ek"
        setContent {
            DroopyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    VideoScreen(VideoViewModel(), token ?: "")
                }
            }
        }
    }
}
