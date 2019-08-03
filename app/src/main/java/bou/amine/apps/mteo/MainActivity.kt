package bou.amine.apps.mteo

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.location.LocationListener
import android.location.LocationManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import bou.amine.apps.mteo.api.DarkSkyApi
import bou.amine.apps.mteo.api.ForecastResponse
import bou.amine.apps.mteo.persistence.database.AppDatabase
import bou.amine.apps.mteo.persistence.entities.LocationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    private val PERM_LOCATION = 101
    private lateinit var db: AppDatabase
    private lateinit var api: DarkSkyApi

    private lateinit var currentLocation: LocationView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        api = DarkSkyApi(this@MainActivity)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "mteo-database"
        ).build()

        thread {
            val locations = db.locationDao().locations()
            runOnUiThread {
                if (locations.isEmpty()) {
                    checkPermission()
                } else {
                    currentLocation = locations[0]
                    fetchForecastData()
                }
            }
        }
    }

    private fun fetchForecastData() {
        thread {
            api.forecast(currentLocation.lat.toString(), currentLocation.lng.toString()).enqueue(object :
                Callback<ForecastResponse> {
                override fun onFailure(call: Call<ForecastResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Can't fetch forecast data !", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<ForecastResponse>, response: Response<ForecastResponse>) {
                    Toast.makeText(this@MainActivity, "Ok", Toast.LENGTH_LONG).show()
                }

            })
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                PERM_LOCATION
            )
        } else {
            handleLocationChange()
        }
    }

    private fun handleLocationChange() {
        val mLocationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        val mLocationListener = object : LocationListener {
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                Toast.makeText(this@MainActivity, "status", Toast.LENGTH_SHORT).show()
            }

            override fun onProviderEnabled(provider: String?) {
                Toast.makeText(this@MainActivity, "enabled", Toast.LENGTH_SHORT).show()
            }

            override fun onProviderDisabled(provider: String?) {
                Toast.makeText(this@MainActivity, "disabled", Toast.LENGTH_SHORT).show()
            }

            override fun onLocationChanged(location: Location) {
                Toast.makeText(this@MainActivity, "location", Toast.LENGTH_SHORT).show()
                mLocationManager.removeUpdates(this)
                currentLocation = LocationView(location.latitude, location.longitude)
                fetchForecastData()
                thread {
                    db.locationDao().insertLocation(currentLocation)
                }
            }
        }

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 10F, mLocationListener)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            PERM_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    handleLocationChange()
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }
}
