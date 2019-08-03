package bou.amine.apps.mteo.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import bou.amine.apps.mteo.persistence.entities.LocationView

@Dao
interface LocationsDao {
    @Query("SELECT * FROM locations order by id asc")
    fun locations(): List<LocationView>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocation(vararg location: LocationView)
}