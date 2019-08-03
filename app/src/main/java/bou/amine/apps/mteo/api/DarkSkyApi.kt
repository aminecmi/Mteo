package bou.amine.apps.mteo.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import bou.amine.apps.mteo.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.Cache
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient


class DarkSkyApi(private val c: Context) {

    private var service: DarkSkyService


    init {
        val cacheSize = (5 * 1024 * 1024).toLong()
        val cache = Cache(c.cacheDir, cacheSize)

        val okHttpClient = OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor { chain ->
                var request = chain.request()
                request = if (hasNetwork(c)!!) {
                    /*
                    *  If there is Internet, get the cache that was stored 2 minutes ago.
                    *  If the cache is older than 2 minutes, then discard it,
                    *  and indicate an error in fetching the response.
                    *  The 'max-age' attribute is responsible for this behavior.
                    */
                    request.newBuilder().header("Cache-Control", "public, max-age=" + (2 * 60)).build()
                } else {
                    /*
                   *  If there is no Internet, get the cache that was stored 7 days ago.
                   *  If the cache is older than 7 days, then discard it,
                   *  and indicate an error in fetching the response.
                   *  The 'max-stale' attribute is responsible for this behavior.
                   *  The 'only-if-cached' attribute indicates to not retrieve new data; fetch the cache only instead.
                   */
                    request.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + (60 * 60 * 24 * 7)).build()
                }
                chain.proceed(request)
            }
            .build()

        val gson =
            GsonBuilder()
                .setLenient()
                .create()


        val retrofit =
            Retrofit
                .Builder()
                .baseUrl("https://api.darksky.net/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        service = retrofit.create(DarkSkyService::class.java)
    }

    fun forecast(lat: String, lng: String): Call<ForecastResponse> =
        service.forecast(BuildConfig.DARK_SKY_API_KEY, lat, lng, "fr", listOf("ca"))

    private fun hasNetwork(context: Context): Boolean? {
        var isConnected: Boolean? = false // Initial Value
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }
}