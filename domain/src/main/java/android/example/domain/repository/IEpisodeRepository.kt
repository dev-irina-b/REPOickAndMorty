package android.example.domain.repository

import android.example.domain.entities.Character
import android.example.domain.entities.Episode
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface IEpisodeRepository {
    fun getEpisodes(
        name: String? = null,
        episode: String? = null,
    ): Flow<PagingData<Episode>>

    suspend fun getEpisode(id: Int): Episode

    suspend fun getMultipleCharacters( arrayIds: List<Int>): List<Character>
}