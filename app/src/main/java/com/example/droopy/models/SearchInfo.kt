package com.example.droopy.models

import android.media.Image
import java.time.LocalDateTime

enum class SearchStatus{
    ACCEPTED, // ordinal int 0
    PENDING,  // ordinal int 1
    FINISHED, // ordinal int 2
}

data class SearchInfo(
    val uuid: String,
    val title: String,
    val description: String,
    val status: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val isPaid: Boolean,
    val imageUrl: String,
){
    val searchStatus: SearchStatus
        get() = when (status) {
            0 -> SearchStatus.ACCEPTED
            1 -> SearchStatus.PENDING
            2 -> SearchStatus.FINISHED
            else -> throw IllegalArgumentException("Invalid status value: $status")
        }
}
