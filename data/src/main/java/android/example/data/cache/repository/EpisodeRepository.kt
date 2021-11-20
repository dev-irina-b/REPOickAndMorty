package android.example.data.cache.repository

import android.example.data.paging.EpisodeRemoteMediator
import android.example.data.cache.database.cache.CharacterCache
import android.example.data.cache.database.cache.EpisodeCache
import android.example.data.network.Network
import android.example.domain.entities.Character
import android.example.domain.entities.Episode
import android.example.domain.repository.IEpisodeRepository
import androidx.paging.*
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EpisodeRepository @Inject constructor(
    private val episodeCache: EpisodeCache,
    private val characterCache: CharacterCache,
    private val network: Network
) : IEpisodeRepository {

    @ExperimentalPagingApi
    override fun getEpisodes(
        name: String?,
        episode: String?
    ): Flow<PagingData<Episode>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true
            ),
            remoteMediator = EpisodeRemoteMediator(
                episodeCache,
                network,
                name,
                episode
            ),
            pagingSourceFactory = { episodeCache.getEpisodes(name, episode) }
        ).flow
    }

    override suspend fun getEpisode(id: Int): Episode {
        return try {
            val response = network.getEpisode(id)
            val networkError = response.errorBody() is IOException
            if (!networkError) {
                response.body()?.let { episodeCache.insertEpisode(it) }
            }
            episodeCache.getEpisode(id)
        } catch (e: Exception) {
            episodeCache.getEpisode(id)
        } catch (e: HttpException) {
            episodeCache.getEpisode(id)
        }
    }

    override suspend fun getMultipleCharacters(arrayIds: List<Int>): List<Character> {
        return try {
            val response = network.getMultipleCharacters(arrayIds)
            response.body()?.let { characterCache.insertCharacters(it) }
            characterCache.getMultipleCharacters(arrayIds)
        } catch (e: Exception) {
            characterCache.getMultipleCharacters(arrayIds)
        } catch (e: HttpException) {
            characterCache.getMultipleCharacters(arrayIds)
        }
    }
}