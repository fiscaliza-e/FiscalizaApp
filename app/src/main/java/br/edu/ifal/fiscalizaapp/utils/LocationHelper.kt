package br.edu.ifal.fiscalizaapp.utils

import android.content.Context
import android.location.Geocoder
import android.os.Build
import br.edu.ifal.fiscalizaapp.data.api.nominatim.NominatimRetrofitHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

data class GeocodedAddress(
    val rua: String = "",
    val bairro: String = "",
    val cep: String = ""
)

suspend fun geocodeLocation(context: Context, latitude: Double, longitude: Double): GeocodedAddress {
    // Nominatim is more reliable for Brazilian addresses (returns suburb/postcode)
    try {
        val result = withContext(Dispatchers.IO) {
            NominatimRetrofitHelper.getAPI().reverse(latitude, longitude)
        }
        val addr = result.address
        if (addr != null) {
            return GeocodedAddress(
                rua = addr.road ?: "",
                bairro = addr.suburb ?: addr.neighbourhood ?: "",
                cep = (addr.postcode ?: "").replace(Regex("[^0-9]"), "")
            )
        }
    } catch (_: Exception) {}

    // Fallback: Android Geocoder
    return try {
        val geocoder = Geocoder(context)
        val addr = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            suspendCancellableCoroutine { cont ->
                geocoder.getFromLocation(latitude, longitude, 1) { addresses ->
                    cont.resume(addresses.firstOrNull())
                }
            }
        } else {
            withContext(Dispatchers.IO) {
                @Suppress("DEPRECATION")
                geocoder.getFromLocation(latitude, longitude, 1)?.firstOrNull()
            }
        }
        GeocodedAddress(
            rua = addr?.thoroughfare ?: "",
            bairro = addr?.subLocality ?: addr?.locality ?: "",
            cep = (addr?.postalCode ?: "").replace(Regex("[^0-9]"), "")
        )
    } catch (_: Exception) {
        GeocodedAddress()
    }
}
