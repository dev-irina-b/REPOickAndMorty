package android.example.domain.interactors

import android.example.domain.entities.Character
import android.example.domain.entities.Episode
import android.example.domain.repository.IEpisodeRepository
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface IEpisodesInteractor {
    fun getEpisodes(
        name: String? = null,
        episode: String? = null,
    ): Flow<PagingData<Episode>>

    suspend fun getEpisode(id: Int): Episode

    suspend fun getMultipleCharacters( arrayIds: List<Int>): List<Character>
}

class EpisodesInteractor @Inject constructor(
    private val episodeRepository: IEpisodeRepository
    ): IEpisodesInteractor {
    override fun getEpisodes(
        name: String?,
        episode: String?
    ): Flow<PagingData<Episode>> =
        episodeRepository.getEpisodes(name, episode)

    override suspend fun getEpisode(id: Int): Episode =
        episodeRepository.getEpisode(id)

    override suspend fun getMultipleCharacters(arrayIds: List<Int>): List<Character> =
        episodeRepository.getMultipleCharacters(arrayIds)
}