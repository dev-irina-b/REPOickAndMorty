package android.example.rickandmorty.viewmodel

import android.example.domain.entities.Character
import android.example.domain.interactors.ICharactersInteractor
import androidx.lifecycle.ViewModel
import androidx.paging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val charactersInteractor: ICharactersInteractor
) : ViewModel() {

    val filterVisible = MutableStateFlow(false)

    var currentResult: Flow<PagingData<Character>>? = null

    @ExperimentalCoroutinesApi
    fun loadCharacters(
        name: String? = null,
        status: String? = null,
        species: String? = null,
        type: String? = null,
        gender: String? = null
    ): Flow<PagingData<Character>> {
        val newResult: Flow<PagingData<Character>> =
                charactersInteractor.getCharacters(name, status, species, type, gender)
        currentResult = newResult
        return newResult
    }

    fun showFilter() {
        filterVisible.value = !filterVisible.value
    }

    @ExperimentalCoroutinesApi
    fun applyFilter(
        name: String,
        status: String,
        species: String,
        type: String,
        gender: String
    ): Flow<PagingData<Character>> {
        filterVisible.value = false

        return loadCharacters(
            if (name.isBlank()) null else name,
            if (status.isBlank() || status == "All") null else status,
            if (species.isBlank()) null else species,
            if (type.isBlank()) null else type,
            if (gender.isBlank() || gender == "All") null else gender
        )
    }
}