package android.example.rickandmorty.viewmodel

import android.example.domain.entities.Character
import android.example.domain.entities.Episode
import android.example.domain.interactors.ICharactersInteractor
import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class CharactersDetailsViewModel @Inject constructor(
    private val charactersInteractor: ICharactersInteractor
) : ViewModel() {

    private lateinit var character: Character

    @InternalCoroutinesApi
    suspend fun getCharacter(id: Int): Character {
        character = charactersInteractor.getCharacter(id)
        return character
    }

    @InternalCoroutinesApi
    suspend fun getMultipleEpisodes(): List<Episode> {
        val arrayIds = getArray()
        return charactersInteractor.getMultipleEpisodes(arrayIds)
    }

    private fun getArray(): List<Int> {
        var arrayIds= listOf<Int>()
        character.let { character ->
            arrayIds = character.episode.map {
                it.substringAfter("episode/").toInt()
            }
        }
        return arrayIds
    }
}