package android.example.data.cache.database.cache

import android.example.data.cache.database.ApplicationDatabase
import android.example.data.entities.Info
import android.example.domain.entities.Episode
import androidx.paging.PagingSource

class EpisodeCache(val database: ApplicationDatabase) {

    suspend fun insertEpisodes(episodes: List<Episode>) =
        database.episodeDao().insertEpisodes(episodes)

    suspend fun insertEpisode(episode: Episode) =
        database.episodeDao().insertEpisode(episode)

    fun getEpisodes(
        name: String? = null,
        episode: String? = null): PagingSource<Int, Episode> =
        database.episodeDao().getEpisodes(name, episode)

    suspend fun getEpisode(id: Int): Episode = database.episodeDao().getEpisode(id)

    suspend fun getMultipleEpisodes(arrayIds: List<Int>): List<Episode> =
        database.episodeDao().getMultipleEpisodes(arrayIds)

    suspend fun clearEpisodes() = database.episodeDao().clearEpisodes()

    suspend fun insertInfo(list: List<Info>) =  database.episodeInfoDao().insertInfo(list)

    suspend fun getNextPage(id: Int): Info? = database.episodeInfoDao().getNextPage(id)

    suspend fun clearInfo() = database.episodeInfoDao().clearInfo()
}