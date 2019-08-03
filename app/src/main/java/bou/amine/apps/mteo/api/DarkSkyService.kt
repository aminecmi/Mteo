package bou.amine.apps.mteo.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface DarkSkyService {

    // https://api.darksky.net/forecast/[key]/[latitude],[longitude]
    @GET("forecast/{apiKey}/{lat},{lng}")
    fun forecast(
        @Path("apiKey") apiKey: String,
        @Path("lat") lat: String,
        @Path("lng") lng: String,
        @Query("lang") lang: String,
        @Query("units") units: List<String>
    ): Call<ForecastResponse>
}