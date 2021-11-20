package android.example.data.paging

import android.example.data.cache.database.cache.EpisodeCache
import android.net.Uri
import androidx.paging.RemoteMediator
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import android.example.data.entities.Info
import android.example.data.network.Network
import android.example.domain.entities.Episode
import androidx.paging.PagingState
import androidx.room.withTransaction
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class EpisodeRemoteMediator(
    private val episodeCache: EpisodeCache,
    private val network: Network,
    private val name: String?,
    private val episode: String?,
) : RemoteMediator<Int, Episode>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Episode>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prev
                    if (prevKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    } else {
                        val prevPageQuery =
                            Uri.parse(remoteKeys.prev).getQueryParameter("page")
                        prevPageQuery?.toInt()
                    }
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.next
                    if (nextKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    } else {
                        val nextPageQuery = Uri.parse(nextKey).getQueryParameter("page")
                        nextPageQuery?.toInt()
                    }
                }
            }

            val response = network.getEpisodes(loadKey, name, episode)
            val resBody = response.body()
            val pageInfo = resBody?.info
            val results = resBody?.results
            val networkError = response.errorBody() is IOException

            episodeCache.database.withTransaction {
                if (loadType == LoadType.REFRESH && !networkError) {
                    episodeCache.clearEpisodes()
                    episodeCache.clearInfo()
                }
                results?.forEach {
                    it.next = pageInfo?.next
                    it.prev = pageInfo?.prev
                }

                val keys = results?.map {
                    Info(id = it.id, prev = it.prev, next = it.next)
                }
                if (keys != null) {
                    episodeCache.insertInfo(keys)
                }
                if (results != null) {
                    episodeCache.insertEpisodes(results)
                }
            }

            MediatorResult.Success(
                endOfPaginationReached = resBody?.info?.next == null
            )
        } catch (e: Exception) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Episode>): Info? {
        return state.lastItemOrNull()?.let { episode ->
            episodeCache.getNextPage(episode.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Episode>): Info? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { episode ->
                episodeCache.getNextPage(episode.id)
            }
    }
}