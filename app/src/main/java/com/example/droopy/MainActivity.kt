package com.example.droopy.video.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.droopy.video.VideoActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, VideoActivity::class.java)
        startActivity(intent)
        finish()
    }
}