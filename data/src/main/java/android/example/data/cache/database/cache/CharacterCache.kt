package android.example.data.cache.database.cache

import android.example.data.cache.database.ApplicationDatabase
import android.example.data.entities.Info
import android.example.domain.entities.Character

class CharacterCache(val database: ApplicationDatabase) {

    suspend fun insertCharacters(characters: List<Character>) =
        database.characterDao().insertCharacters(characters)

    suspend fun insertCharacter(character: Character) =
        database.characterDao().insertCharacter(character)

    fun getCharacters(
        name: String?,
        status: String?,
        species: String?,
        type: String?,
        gender: String?
    ) = database.characterDao().getCharacters(name, status, species, type, gender)

    suspend fun getCharacter(id: Int): Character = database.characterDao().getCharacter(id)

    suspend fun getMultipleCharacters(arrayIds: List<Int>): List<Character> =
        database.characterDao().getMultipleCharacters(arrayIds)

    suspend fun clearCharacters() = database.characterDao().clearCharacters()

    suspend fun insertInfo(list: List<Info>) = database.characterInfoDao().insertInfo(list)

    suspend fun getNextPage(id: Int): Info? = database.characterInfoDao().getNextPage(id)

    suspend fun clearInfo() = database.characterInfoDao().clearInfo()
}