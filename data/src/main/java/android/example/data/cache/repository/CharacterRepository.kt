package android.example.data.cache.repository

import android.example.data.paging.CharacterRemoteMediator
import android.example.data.cache.database.cache.CharacterCache
import android.example.data.cache.database.cache.EpisodeCache
import android.example.data.network.Network
import androidx.paging.*
import kotlinx.coroutines.flow.Flow
import android.example.domain.entities.Character
import android.example.domain.entities.Episode
import android.example.domain.repository.ICharacterRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepository @Inject constructor(
    private val characterCache: CharacterCache,
    private val episodeCache: EpisodeCache,
    private val network: Network
) : ICharacterRepository {

    @ExperimentalPagingApi
    override fun getCharacters(
        name: String?,
        status: String?,
        species: String?,
        type: String?,
        gender: String?
    ): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true
            ),
            remoteMediator = CharacterRemoteMediator(
                characterCache,
                network,
                name,
                status,
                species,
                type,
                gender
            ),
            pagingSourceFactory = {
                characterCache.getCharacters(name, status, species, type, gender)
            }
        ).flow
    }

    override suspend fun getCharacter(id: Int): Character {
        return try {
            val response = network.getCharacter(id)
            val networkError = response.errorBody() is IOException
            if (!networkError) {
                response.body()?.let { characterCache.insertCharacter(it) }
            }
            characterCache.getCharacter(id)
        } catch (e: Exception) {
            characterCache.getCharacter(id)
        } catch (e: HttpException) {
            characterCache.getCharacter(id)
        }
    }

    override suspend fun getMultipleEpisodes(arrayIds: List<Int>): List<Episode> {
        return try {
            val response = network.getMultipleEpisodes(arrayIds)
            response.body()?.let { episodeCache.insertEpisodes(it) }
            episodeCache.getMultipleEpisodes(arrayIds)
        } catch (e: Exception) {
            episodeCache.getMultipleEpisodes(arrayIds)
        } catch (e: HttpException) {
            episodeCache.getMultipleEpisodes(arrayIds)
        }
    }
}