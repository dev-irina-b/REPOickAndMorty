package android.example.data.cache.database.cache

import android.example.data.cache.database.ApplicationDatabase
import android.example.data.entities.Info
import android.example.domain.entities.Location
import androidx.paging.PagingSource

class LocationCache(val database: ApplicationDatabase) {

    suspend fun insertLocations(locations: List<Location>) =
        database.locationDao().insertLocations(locations)

    suspend fun insertLocation(location: Location) =
        database.locationDao().insertLocation(location)

    fun getLocations(
        name: String? = null,
        type: String? = null,
        dimension: String? = null)
    : PagingSource<Int, Location> = database.locationDao().getLocations(name, type, dimension)

    suspend fun getLocation(id: Int): Location = database.locationDao().getLocation(id)

    suspend fun clearLocations() = database.locationDao().clearLocations()

    suspend fun insertInfo(list: List<Info>) = database.locationInfoDao().insertInfo(list)

    suspend fun getNextPage(id: Int): Info? = database.locationInfoDao().getNextPage(id)

    suspend fun clearInfo() = database.locationInfoDao().clearInfo()
}