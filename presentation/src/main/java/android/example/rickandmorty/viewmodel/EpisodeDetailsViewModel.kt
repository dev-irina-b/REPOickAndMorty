package android.example.rickandmorty.viewmodel

import android.example.domain.entities.Character
import android.example.domain.entities.Episode
import android.example.domain.interactors.IEpisodesInteractor
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EpisodeDetailsViewModel @Inject constructor(
    private val episodesInteractor: IEpisodesInteractor
): ViewModel() {

    private lateinit var episode: Episode

    suspend fun getEpisode(id: Int): Episode {
        episode = episodesInteractor.getEpisode(id)
        return episode
    }

    suspend fun getMultipleCharacters(): List<Character> {
        val arrayIds = getArray()
        return episodesInteractor.getMultipleCharacters(arrayIds)
    }

    private fun getArray(): List<Int> {
        var arrayId= listOf<Int>()
        episode.let { episode ->
            arrayId = episode.characters.map {
                it.substringAfter("character/").toInt()
            }
        }
        return arrayId
    }
}