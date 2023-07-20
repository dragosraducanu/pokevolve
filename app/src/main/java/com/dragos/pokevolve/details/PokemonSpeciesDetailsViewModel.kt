package com.dragos.pokevolve.details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.dragos.domain.model.PokemonSpecies
import com.dragos.domain.usecase.GetNextSpeciesInEvolutionChainUseCase
import com.dragos.domain.usecase.GetPokemonSpeciesByIdUseCase
import com.dragos.domain.util.UrlUtils
import com.dragos.pokevolve.util.plusAssign
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class PokemonSpeciesDetailsViewModel @Inject constructor(
    private val getPokemonSpeciesByIdUseCase: GetPokemonSpeciesByIdUseCase,
    private val getNextSpeciesInEvolutionChainUseCase: GetNextSpeciesInEvolutionChainUseCase,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    private val _screenState = mutableStateOf(ScreenState())
    val screenState: State<ScreenState> = _screenState

    fun loadPokemonSpecies(id: Int) {
        compositeDisposable += getPokemonSpeciesByIdUseCase(id)
            .doOnSubscribe {
                _screenState.value = _screenState.value.copy(isLoading = true)
            }
            .subscribe({
                _screenState.value = _screenState.value.copy(
                    isLoading = false,
                    baseSpecies = SpeciesItem(id, it)
                )

                val chainId = UrlUtils.getIdFromUrl(it.evolutionChain.url)
                if (chainId != null) {
                    loadNextSpeciesInEvolutionChain(it.captureRate, chainId, id)
                } else {
                    // no evo chain?
                }

            }, {
                _screenState.value = _screenState.value.copy(
                    isLoading = false,
                    errorBaseSpecies = true
                )
            })
    }



    private fun loadNextSpeciesInEvolutionChain(captureRate: Int, chainId: Int, baseSpeciesId: Int) {
        compositeDisposable += getNextSpeciesInEvolutionChainUseCase(baseSpeciesId, chainId)
            .doOnSubscribe {
                _screenState.value = _screenState.value.copy(isLoadingEvolutionChain = true)
            }
            .subscribe({evolutionSpeciesId ->
                if (evolutionSpeciesId != -1) {
                    getCaptureRateDiff(captureRate, evolutionSpeciesId)
                } else {
                    _screenState.value = _screenState.value.copy(
                        isLoadingEvolutionChain = false,
                        doesNotEvolve = true
                    )
                }

            }, {
                _screenState.value = _screenState.value.copy(
                    isLoadingEvolutionChain = false,
                    errorEvolutionSpecies = true
                )
            })
    }

    private fun getCaptureRateDiff(captureRate: Int, id: Int) {
        compositeDisposable += getPokemonSpeciesByIdUseCase(id)
            .doOnSubscribe {}
            .subscribe({
                _screenState.value = _screenState.value.copy(
                    isLoadingEvolutionChain = false,
                    captureRateDifference = it.captureRate - captureRate,
                    evolutionSpecies = SpeciesItem(id, it)
                )
            }, {})
    }


    data class ScreenState(
        val isLoading: Boolean = false,
        val isLoadingEvolutionChain: Boolean = false,
        val errorBaseSpecies: Boolean = false,
        val errorEvolutionSpecies: Boolean = false,

        val baseSpecies: SpeciesItem? = null,
        val evolutionSpecies: SpeciesItem? = null,
        val doesNotEvolve: Boolean = false,
        val captureRateDifference: Int? = null
    )

    data class SpeciesItem(
        val id: Int,
        val name: String,
        val flavorText: String
    ) {
        constructor(id: Int, pokemonSpecies: PokemonSpecies) : this(
            id,
            pokemonSpecies.name,
            pokemonSpecies.flavorTextEntries.first { flavor ->
                flavor.language.name == "en"
            }.flavorText.replace("\n", " ")
        )
    }
}