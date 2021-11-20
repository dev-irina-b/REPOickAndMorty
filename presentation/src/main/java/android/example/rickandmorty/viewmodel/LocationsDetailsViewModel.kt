package android.example.rickandmorty.viewmodel

import android.example.domain.entities.Character
import android.example.domain.entities.Location
import android.example.domain.interactors.ILocationsInteractor
import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class LocationsDetailsViewModel @Inject constructor(
    private val locationsInteractor: ILocationsInteractor
) : ViewModel() {

    private lateinit var location: Location

    suspend fun getLocation(id: Int): Location {
        location = locationsInteractor.getLocation(id)
        return location
    }

    suspend fun getMultipleCharacters(): List<Character> {
        val arrayIds = getArray()
        return locationsInteractor.getMultipleCharacters(arrayIds)
    }

    private fun getArray(): List<Int> {
        var arrayId= listOf<Int>()
        location.let { location ->
            arrayId = location.residents.map {
                it.substringAfter("character/").toInt()
            }
        }
        return arrayId
    }
}