package com.example.droopy.ui.api

import com.example.droopy.models.FilmSearchesResponse
import com.example.droopy.models.LoginRequestBody
import com.example.droopy.models.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequestBody): LoginResponse

    @GET("api/film_search/me/consumer")
    suspend fun getFilmSearchConsumer(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Header("Authorization") authorization: String
    ): List<FilmSearchesResponse>
}