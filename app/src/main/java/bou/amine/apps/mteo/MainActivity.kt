package bou.amine.apps.mteo

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import bou.amine.apps.mteo.api.DarkSkyApi
import bou.amine.apps.mteo.api.ForecastResponse
import bou.amine.apps.mteo.persistence.MIGRATION_1_2
import bou.amine.apps.mteo.persistence.database.AppDatabase
import bou.amine.apps.mteo.persistence.entities.LocationView
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.badgeable.secondaryItem
import co.zsmb.materialdrawerkt.draweritems.divider
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.mikepenz.materialdrawer.Drawer
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    private val PERM_LOCATION = 101
    private lateinit var db: AppDatabase
    private lateinit var api: DarkSkyApi

    private lateinit var currentLocation: LocationView
    private lateinit var locations: List<LocationView>

    private lateinit var drawer: Drawer

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(bottom_app_bar)

        api = DarkSkyApi(this@MainActivity)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "mteo-database"
        ).addMigrations(MIGRATION_1_2).build()

        fab.setOnClickListener { checkPermission() }

        handleRefresh()

        thread {
            locations = db.locationDao().locations()
            runOnUiThread {
                if (locations.isEmpty()) {
                    checkPermission()
                } else {
                    selectLocationAndFetch()
                    loadDrawer()
                    drawer.setSelection(locations[0].id.toLong(), false)
                }
            }
        }
    }

    private fun handleRefresh() {
        swipeRefreshLayout.setColorSchemeResources(
            R.color.colorPrimary,
            R.color.colorPrimaryDark,
            R.color.colorAccent
        )
        swipeRefreshLayout.setOnRefreshListener {
            fetchForecastData()
        }
    }

    private fun loadDrawer() {
        if (::drawer.isInitialized) {
            drawer.closeDrawer()
        }
        drawer =  drawer {
            toolbar = toolBar
            primaryItem("Locations") {
                selectable = false
            }
            locations.map {
                secondaryItem(it.name) {
                    identifier = it.id.toLong()
                    onClick { _, position, _ ->
                        selectLocationAndFetch(position - 1)
                        true
                    }
                }
            }
            onItemLongClick {_, position, _ ->
                val toRemove = locations[position - 1]
                locations = locations.filterNot { it == toRemove }
                loadDrawer()
                thread {
                    db.locationDao().deleteLocation(toRemove)
                }
                true
            }
        }
    }

    private fun selectLocationAndFetch(position: Int = 0) {
        swipeRefreshLayout.isRefreshing = true
        currentLocation = locations[position]
        fetchForecastData()
    }

    private fun fetchForecastData() {
        thread {
            api.forecast(currentLocation.lat.toString(), currentLocation.lng.toString()).enqueue(object :
                Callback<ForecastResponse> {
                override fun onFailure(call: Call<ForecastResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Can't fetch forecast data !", Toast.LENGTH_LONG).show()
                    swipeRefreshLayout.isRefreshing = false
                }

                override fun onResponse(call: Call<ForecastResponse>, response: Response<ForecastResponse>) {
                    Toast.makeText(this@MainActivity, "Ok", Toast.LENGTH_LONG).show()
                    swipeRefreshLayout.isRefreshing = false
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
                MaterialDialog(this@MainActivity).show {
                    input { _, text ->
                        locations += LocationView(location.latitude, location.longitude, text.toString())
                        selectLocationAndFetch(locations.size - 1)
                        thread {
                            db.locationDao().insertLocation(currentLocation)
                            runOnUiThread {
                                loadDrawer()
                            }
                        }
                    }
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.bottom_app_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.alerts -> Toast.makeText(this@MainActivity, "alerts item is clicked!", Toast.LENGTH_SHORT).show()
            R.id.currently -> Toast.makeText(this@MainActivity, "currently item is clicked!", Toast.LENGTH_SHORT).show()
            R.id.daily -> Toast.makeText(this@MainActivity, "daily item is clicked!", Toast.LENGTH_SHORT).show()
            R.id.hourly -> Toast.makeText(this@MainActivity, "hourly item is clicked!", Toast.LENGTH_SHORT).show()
            R.id.minutely -> Toast.makeText(this@MainActivity, "minutely item is clicked!", Toast.LENGTH_SHORT).show()
        }

        return true
    }
}
