package com.dragos.pokevolve

import com.dragos.domain.model.APIResource
import com.dragos.domain.model.FlavorTextEntry
import com.dragos.domain.model.NamedAPIResource
import com.dragos.domain.model.PokemonSpecies
import com.dragos.domain.usecase.GetNextSpeciesInEvolutionChainUseCase
import com.dragos.domain.usecase.GetPokemonSpeciesByIdUseCase
import com.dragos.pokevolve.details.PokemonSpeciesDetailsViewModel
import com.dragos.testing.RxTestSchedulerRule
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.MockitoRule

@RunWith(MockitoJUnitRunner::class)
class SpeciesDetailsViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val rxTestSchedulerRule = RxTestSchedulerRule()

    @Mock
    private lateinit var getSpeciesByIdUseCase: GetPokemonSpeciesByIdUseCase

    @Mock
    private lateinit var getNextInEvolutionChainUseCase: GetNextSpeciesInEvolutionChainUseCase

    @Mock
    private lateinit var compositeDisposable: CompositeDisposable

    private val baseSpecies = PokemonSpecies(
        name = "species-1",
        captureRate = 20,
        flavorTextEntries = listOf(
            FlavorTextEntry(
                flavorText = "Flavor text",
                NamedAPIResource("en", "https://example.com/language/1"))
        ),
        evolutionChain = APIResource("https://example.com/chain/1")
    )

    private val firstEvolutionSpecies = PokemonSpecies(
        name = "species-2",
        captureRate = 30,
        flavorTextEntries = listOf(
            FlavorTextEntry(
                flavorText = "Flavor text",
                NamedAPIResource("en", "https://example.com/language/1"))
        ),
        evolutionChain = APIResource("https://example.com/chain/1")
    )

    private lateinit var viewModel: PokemonSpeciesDetailsViewModel

    @Before
    fun setup() {
        `when`(getSpeciesByIdUseCase.invoke(1)).thenReturn(
            Single.just(baseSpecies)
        )

        `when`(getSpeciesByIdUseCase.invoke(2)).thenReturn(
            Single.just(firstEvolutionSpecies)
        )

        `when`(getNextInEvolutionChainUseCase.invoke(1, 1)).thenReturn(
            Single.just(2)
        )

        viewModel = PokemonSpeciesDetailsViewModel(getSpeciesByIdUseCase, getNextInEvolutionChainUseCase, compositeDisposable)
        viewModel.loadPokemonSpecies(1)

    }

    @Test
    fun `test that base and first evolution species are loaded`() {
        Assert.assertEquals("species-1", viewModel.screenState.value.baseSpecies!!.name)
        Assert.assertEquals("species-2", viewModel.screenState.value.evolutionSpecies!!.name)
    }

    @Test
    fun `test that the capture rate is 10`() {
        Assert.assertEquals(10, viewModel.screenState.value.captureRateDifference)
    }
}