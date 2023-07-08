package com.example.droopy.models

data class FilmSearch(
	val uuid: String,
	val location: Location? = null,
)

data class Location(
	val latitude: String? = null,
	val longitude: String? = null,
	val address: String? = null,
	val description: String? = null
)

