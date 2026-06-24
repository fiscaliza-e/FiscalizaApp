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

    @GET("reverse")
    suspend fun reverse(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("format") format: String = "json",
        @Query("addressdetails") addressDetails: Int = 1
    ): NominatimReverseResultDTO
}

data class NominatimResultDTO(
    val lat: String,
    val lon: String,
    val displayName: String = ""
)

data class NominatimReverseResultDTO(
    val address: NominatimAddressDTO? = null
)

data class NominatimAddressDTO(
    val road: String? = null,
    val suburb: String? = null,
    val neighbourhood: String? = null,
    val postcode: String? = null,
    val city: String? = null,
    val town: String? = null
)
