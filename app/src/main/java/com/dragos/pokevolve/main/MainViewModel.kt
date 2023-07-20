package com.dragos.pokevolve.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.dragos.domain.model.NamedAPIResource
import com.dragos.domain.usecase.GetPokemonSpeciesListUseCase
import com.dragos.pokevolve.util.plusAssign
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getPokemonSpeciesList: GetPokemonSpeciesListUseCase,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    private val _screenState = mutableStateOf(ScreenState())
    val screenState: State<ScreenState> = _screenState

    fun loadPokemonSpecies() {
        val totalLoaded = _screenState.value.pokemonSpeciesList.count()
        val totalToLoad = _screenState.value.totalPokemonSpecies

        if (totalToLoad != -1 && totalLoaded >= totalToLoad) {
            return
        }

        compositeDisposable += getPokemonSpeciesList(_screenState.value.pokemonSpeciesList.count())
            .doOnSubscribe {
                val isLoadingFirstPage = totalLoaded == 0
                _screenState.value = _screenState.value.copy(
                    isLoading = isLoadingFirstPage,
                    isLoadingNextPage = !isLoadingFirstPage
                )
            }
            .subscribe({ result ->
                _screenState.value = _screenState.value.copy(
                    isLoading = false,
                    isLoadingNextPage = false,
                    totalPokemonSpecies = result.count,
                    pokemonSpeciesList = _screenState.value.pokemonSpeciesList + result.results,
                    error = result.results.isEmpty()
                )
            }, {
                _screenState.value = _screenState.value.copy(
                    isLoading = false,
                    isLoadingNextPage = false,
                    error = true
                )
            })
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
    data class ScreenState(
        val isLoading: Boolean = false,
        val isLoadingNextPage: Boolean = false,
        val pokemonSpeciesList: List<NamedAPIResource> = emptyList(),
        val totalPokemonSpecies: Int = -1,
        val error: Boolean = false
    )
}