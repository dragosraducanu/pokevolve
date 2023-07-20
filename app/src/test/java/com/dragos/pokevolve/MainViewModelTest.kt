package com.dragos.pokevolve

import com.dragos.domain.model.NamedAPIResource
import com.dragos.domain.model.NamedAPIResourceList
import com.dragos.domain.usecase.GetPokemonSpeciesListUseCase
import com.dragos.pokevolve.main.MainViewModel
import com.dragos.testing.RxTestSchedulerRule
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.MockitoRule

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val rxTestSchedulerRule = RxTestSchedulerRule()

    @Mock
    private lateinit var useCaseMock: GetPokemonSpeciesListUseCase

    @Mock
    private lateinit var compositeDisposable: CompositeDisposable

    private val allSpecies = IntRange(1, 100).map {
        NamedAPIResource(
            name = "poke-${it}",
            url = "https://example.com/${it}"
        )
    }

    @Test
    fun `test that loadPage() uses offset 0 for the first page load`() {
        val result = NamedAPIResourceList(
            count = allSpecies.count(),
            next = "",
            prev = "",
            results = allSpecies.subList(0, 21)
        )

        `when`(useCaseMock.invoke(0)).thenReturn(
            Single.just(result)
        )

        val viewModel = MainViewModel(useCaseMock, compositeDisposable)
        viewModel.loadPokemonSpecies()

        Mockito.verify(useCaseMock).invoke(0)
    }

    @Test
    fun `test that the screen state contains the first 20 species`() {
        val result = NamedAPIResourceList(
            count = allSpecies.count(),
            next = "",
            prev = "",
            results = allSpecies.subList(0, 20)
        )

        `when`(useCaseMock.invoke(0)).thenReturn(
            Single.just(result)
        )

        val viewModel = MainViewModel(useCaseMock, compositeDisposable)
        viewModel.loadPokemonSpecies()

        val expectedState =
            MainViewModel.ScreenState(
                isLoading = false,
                isLoadingNextPage = false,
                pokemonSpeciesList = result.results,
                totalPokemonSpecies = allSpecies.count(),
                error = false
            )

        Assert.assertEquals(expectedState, viewModel.screenState.value)
    }

    @Test
    fun `test that loadPage() uses offset the correct offset for the next page load`() {
        val firstPage = NamedAPIResourceList(
            count = allSpecies.count(),
            next = "",
            prev = "",
            results = allSpecies.subList(0, 20)
        )

        val secondPage = NamedAPIResourceList(
            count = allSpecies.count(),
            next = "",
            prev = "",
            results = allSpecies.subList(20, 40)
        )

        `when`(useCaseMock.invoke(0)).thenReturn(
            Single.just(firstPage)
        )

        `when`(useCaseMock.invoke(20)).thenReturn(
            Single.just(secondPage)
        )

        val viewModel = MainViewModel(useCaseMock, compositeDisposable)
        viewModel.loadPokemonSpecies()
        Mockito.verify(useCaseMock).invoke(0)

        viewModel.loadPokemonSpecies()
        Mockito.verify(useCaseMock).invoke(20)
    }
}