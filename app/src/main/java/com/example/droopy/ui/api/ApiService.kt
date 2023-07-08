package com.example.droopy.ui.api

import com.example.droopy.models.*
import retrofit2.http.*

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequestBody): LoginResponse

    @GET("api/film_search/me/consumer")
    suspend fun getFilmSearchConsumer(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Header("Authorization") authorization: String
    ): List<FilmSearchesResponse>

    @POST("film_postulation/{film_postulation_id}/video-call")
    suspend fun getVideoAndChatTokens(
        @Path("film_postulation_id") filmPostulationId: String,
        @Header("Authorization") authorization: String
    ):CallTokenResponse

    @GET("film_postulation/accepted")
    suspend fun getFilmPostulation(
        @Query("filmSearch") filmSearchId: String,
        @Header("Authorization") authorization: String
    ): FilmPostulationResponse

    @POST("film_search/{film_search_id}/finish")
    suspend fun finishCall(
        @Path("film_search_id") filmSearchId: String,
        @Header("Authorization") authorization: String
    ): Void
}