package android.example.domain.interactors

import android.example.domain.entities.Character
import android.example.domain.entities.Episode
import android.example.domain.repository.ICharacterRepository
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ICharactersInteractor {
    fun getCharacters(
        name: String?,
        status: String?,
        species: String?,
        type: String?,
        gender: String?
    ): Flow<PagingData<Character>>

    suspend fun getCharacter(id: Int): Character

    suspend fun getMultipleEpisodes(arrayIds: List<Int>): List<Episode>
}

class CharactersInteractor @Inject constructor(
    private val characterRepository: ICharacterRepository
    ): ICharactersInteractor {
    override fun getCharacters(
        name: String?,
        status: String?,
        species: String?,
        type: String?,
        gender: String?
    ): Flow<PagingData<Character>> =
        characterRepository.getCharacters(name, status, species, type, gender)

    override suspend fun getCharacter(id: Int): Character =
        characterRepository.getCharacter(id)

    override suspend fun getMultipleEpisodes(arrayIds: List<Int>): List<Episode> =
        characterRepository.getMultipleEpisodes(arrayIds)
}