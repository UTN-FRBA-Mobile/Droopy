package com.example.droopy.models

import android.media.Image
import java.time.LocalDateTime

data class SearchInfo(
    val uuid: String,
    val title: String,
    val description: String,
    val status: String,
    val startDate: LocalDateTime,
    val isPaid: Boolean,
    val image: Int
)
