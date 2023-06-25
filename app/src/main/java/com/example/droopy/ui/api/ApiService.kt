package com.example.droopy.ui.api

import com.example.droopy.models.FilmSearch
import com.example.droopy.models.LoginRequestBody
import com.example.droopy.models.LoginResponse
import com.example.droopy.models.SearchInfo
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequestBody): LoginResponse

    @GET("api/film_search/me/consumer")
    suspend fun getFilmSearchConsumer(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Header("Authorization") authorization: String
    ): List<FilmSearch>

    @GET("api/film_search/{uuid}")
    suspend fun getFilmSearchById(
        @Path("uuid") filmSearchId: String,
        @Header("Authorization") authorization: String
    ): SearchInfo
}