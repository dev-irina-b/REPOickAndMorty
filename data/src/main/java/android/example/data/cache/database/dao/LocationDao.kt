package android.example.data.cache.database.dao

import android.example.domain.entities.Location
import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocations(locations: List<Location>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: Location)

    @Query("SELECT * FROM locations WHERE (:name IS NULL OR name LIKE '%' || :name || '%') AND (:type IS NULL OR type = :type) AND (:dimension IS NULL OR dimension = :dimension) ")
    fun getLocations(
        name: String? = null,
        type: String? = null,
        dimension: String? = null): PagingSource<Int, Location>

    @Query("SELECT * FROM locations WHERE id = :id")
    suspend fun getLocation(id: Int): Location

    @Query("DELETE FROM locations")
    suspend fun clearLocations()
}