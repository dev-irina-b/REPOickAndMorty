package android.example.domain.interactors

import android.example.domain.entities.Character
import android.example.domain.entities.Location
import android.example.domain.repository.ILocationRepository
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ILocationsInteractor {
    fun getLocations(
        name: String? = null,
        type: String? = null,
        dimension: String? = null,
    ): Flow<PagingData<Location>>

    suspend fun getLocation(id: Int): Location

    suspend fun getMultipleCharacters( arrayIds: List<Int>): List<Character>
}

class LocationsInteractor @Inject constructor(
    private val locationIRepository: ILocationRepository
    ) : ILocationsInteractor {
    override fun getLocations(
        name: String?,
        type: String?,
        dimension: String?
    ): Flow<PagingData<Location>> =
        locationIRepository.getLocations(name, type, dimension)

    override suspend fun getLocation(id: Int): Location =
        locationIRepository.getLocation(id)

    override suspend fun getMultipleCharacters(arrayIds: List<Int>): List<Character> =
        locationIRepository.getMultipleCharacters(arrayIds)
}