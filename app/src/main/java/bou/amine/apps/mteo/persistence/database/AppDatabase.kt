package bou.amine.apps.mteo.persistence.database

import androidx.room.Database
import androidx.room.RoomDatabase
import bou.amine.apps.mteo.persistence.dao.LocationsDao
import bou.amine.apps.mteo.persistence.entities.LocationView

@Database(entities = [LocationView::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationsDao
}