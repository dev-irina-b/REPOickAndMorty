package android.example.rickandmorty.viewmodel

import android.example.domain.entities.Episode
import android.example.domain.interactors.IEpisodesInteractor
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
class EpisodesViewModel @Inject constructor(
    private val episodesInteractor: IEpisodesInteractor
): ViewModel(){

    val filterVisible = MutableStateFlow(false)

    private var currentResult: Flow<PagingData<Episode>>? = null

    @ExperimentalCoroutinesApi
    @ExperimentalPagingApi
    fun loadEpisodes(
        name: String? = null,
        episode: String? = null
    ): Flow<PagingData<Episode>> {
        val newResult: Flow<PagingData<Episode>> =
            episodesInteractor.getEpisodes(name, episode)
        currentResult = newResult
        return newResult
    }

    fun showFilter()  {
        filterVisible.value = !filterVisible.value
    }

    @ExperimentalCoroutinesApi
    fun applyFilter(
        name: String,
        episode: String,
    ): Flow<PagingData<Episode>> {
        filterVisible.value = false

        return loadEpisodes(
            if (name.isBlank()) null else name,
            if (episode.isBlank()) null else episode)
    }
}