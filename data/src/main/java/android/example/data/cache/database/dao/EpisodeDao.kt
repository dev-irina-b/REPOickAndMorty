package android.example.data.cache.database.dao

import android.example.domain.entities.Episode
import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface EpisodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisodes(episodes: List<Episode>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisode(episode: Episode)

    @Query("SELECT * FROM episodes WHERE (:name IS NULL OR name LIKE '%' || :name || '%') AND (:episode IS NULL OR episode = :episode)")
    fun getEpisodes(name: String? = null, episode: String? = null): PagingSource<Int, Episode>

    @Query("SELECT * FROM episodes WHERE id = :id")
    suspend fun getEpisode(id: Int): Episode

    @Query("SELECT * FROM episodes WHERE id in (:arrayIds)")
    suspend fun getMultipleEpisodes(arrayIds: List<Int>): List<Episode>

    @Query("DELETE FROM episodes")
    suspend fun clearEpisodes()
}