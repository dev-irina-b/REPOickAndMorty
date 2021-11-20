package android.example.data.cache.repository

import android.example.data.paging.LocationRemoteMediator
import android.example.data.cache.database.cache.CharacterCache
import android.example.data.cache.database.cache.LocationCache
import android.example.data.network.Network
import android.example.domain.entities.Character
import android.example.domain.entities.Location
import android.example.domain.repository.ILocationRepository
import androidx.paging.*
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepository @Inject constructor(
    private val locationCache: LocationCache,
    private val characterCache: CharacterCache,
    private val network: Network
) : ILocationRepository {

    @ExperimentalPagingApi
    override fun getLocations(
        name: String?,
        type: String?,
        dimension: String?
    ): Flow<PagingData<Location>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true
            ),
            remoteMediator = LocationRemoteMediator(
                locationCache,
                network,
                name,
                type,
                dimension
            ),
            pagingSourceFactory = { locationCache.getLocations(name, type, dimension) }
        ).flow
    }

    override suspend fun getLocation(id: Int): Location {
        return try {
            val response = network.getLocation(id)
            val networkError = response.errorBody() is IOException
            if (!networkError) {
                response.body()?.let { locationCache.insertLocation(it) }
            }
            locationCache.getLocation(id)
        } catch (e: Exception) {
            locationCache.getLocation(id)
        } catch (e: HttpException) {
            locationCache.getLocation(id)
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