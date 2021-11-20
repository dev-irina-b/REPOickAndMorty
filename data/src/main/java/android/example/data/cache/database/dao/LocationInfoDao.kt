package android.example.data.cache.database.dao

import android.example.data.entities.Info
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LocationInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInfo(list: List<Info>)

    @Query("SELECT * FROM info WHERE id LIKE :id")
    suspend fun getNextPage(id: Int): Info?

    @Query("DELETE FROM info")
    suspend fun clearInfo()
}