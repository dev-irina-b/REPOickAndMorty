package android.example.domain.repository

import android.example.domain.entities.Character
import android.example.domain.entities.Location
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface ILocationRepository {
    fun getLocations(
        name: String? = null,
        type: String? = null,
        dimension: String? = null,
    ): Flow<PagingData<Location>>

    suspend fun getLocation(id: Int): Location

    suspend fun getMultipleCharacters( arrayIds: List<Int>): List<Character>
}