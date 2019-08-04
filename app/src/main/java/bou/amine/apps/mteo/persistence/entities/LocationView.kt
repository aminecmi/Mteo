package bou.amine.apps.mteo.persistence.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class LocationView(
    @ColumnInfo(name = "lat")
    val lat: Double,
    @ColumnInfo(name = "lng")
    val lng: Double,
    @ColumnInfo(name = "name")
    val name: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}