package android.example.rickandmorty.viewmodel

import android.example.domain.entities.Location
import android.example.domain.interactors.ILocationsInteractor
import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class
LocationsViewModel @Inject constructor(
    private val locationsInteractor: ILocationsInteractor
) : ViewModel() {

    val filterVisible = MutableStateFlow(false)

    private var currentResult: Flow<PagingData<Location>>? = null

    @ExperimentalCoroutinesApi
    fun loadLocations(
        name: String? = null,
        type: String? = null,
        dimension: String? = null
    ): Flow<PagingData<Location>> {
        val newResult: Flow<PagingData<Location>> =
            locationsInteractor.getLocations(name, type, dimension)
        currentResult = newResult
        return newResult
    }

    fun showFilter()  {
        filterVisible.value = !filterVisible.value
    }

    @ExperimentalCoroutinesApi
    fun applyFilter(
        name: String,
        type: String,
        dimension: String
    ): Flow<PagingData<Location>> {
        filterVisible.value = false
        return loadLocations(
            if (name.isBlank()) null else name,
            if (type.isBlank()) null else type,
            if (dimension.isBlank()) null else dimension)
    }
}