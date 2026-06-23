package br.edu.ifal.fiscalizaapp.data.api.nominatim

import retrofit2.http.GET
import retrofit2.http.Query

interface NominatimAPI {
    @GET("search")
    suspend fun search(
        @Query("q") query: String,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 1
    ): List<NominatimResultDTO>
}

data class NominatimResultDTO(
    val lat: String,
    val lon: String,
    val displayName: String = ""
)
