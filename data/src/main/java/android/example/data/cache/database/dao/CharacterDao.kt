package android.example.data.cache.database.dao

import androidx.paging.PagingSource
import androidx.room.*
import android.example.domain.entities.Character

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<Character>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: Character)

    @Query("SELECT * FROM characters WHERE (:name IS NULL OR name LIKE '%' || :name || '%') AND (:status IS NULL OR status = :status) AND (:species IS NULL OR species = :species) AND (:type IS NULL OR type = :type) AND (:gender IS NULL OR gender = :gender)")
    fun getCharacters(
        name: String? = null,
        status: String? = null,
        species: String? = null,
        type: String? = null,
        gender: String? = null)
    : PagingSource<Int, Character>

    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getCharacter(id: Int): Character

    @Query("SELECT * FROM characters WHERE id in (:arrayIds)")
    suspend fun getMultipleCharacters(arrayIds: List<Int>): List<Character>

    @Query("DELETE FROM characters")
    suspend fun clearCharacters()
}