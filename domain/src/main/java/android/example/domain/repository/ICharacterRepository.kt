package android.example.domain.repository

import android.example.domain.entities.Character
import android.example.domain.entities.Episode
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface ICharacterRepository {
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