package com.dragos.domain

import com.dragos.domain.model.ChainLink
import com.dragos.domain.model.EvolutionChain
import com.dragos.domain.model.NamedAPIResource
import com.dragos.domain.repository.EvolutionChainRepository
import com.dragos.domain.usecase.GetNextSpeciesInEvolutionChainUseCase
import com.dragos.testing.RxTestSchedulerRule
import io.reactivex.Single
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
class GetNextSpeciesInEvolutionChainUseCaseTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val rxTestSchedulerRule = RxTestSchedulerRule()

    @Mock
    private lateinit var repository: EvolutionChainRepository

    private val evolutionChain = EvolutionChain(
        chain = ChainLink(
            evolvesTo = listOf(
                ChainLink(
                    evolvesTo = listOf(
                        ChainLink(
                            evolvesTo = emptyList(),
                            species = NamedAPIResource(
                                name = "species-3",
                                url = "https://example.com/pokemon-species/3"
                            )
                        )
                    ),
                    species = NamedAPIResource(name = "species-2", url = "https://example.com/pokemon-species/2")
                )
            ),
            species = NamedAPIResource(
                name = "species-1",
                url = "https://example.com/pokemon-species/1"
            ),
        )
    )

    private lateinit var useCase: GetNextSpeciesInEvolutionChainUseCase

    @Before
    fun setup() {
        `when`(repository.getEvolutionChain(1)).thenReturn(Single.just(evolutionChain))
        useCase = GetNextSpeciesInEvolutionChainUseCase(repository)
    }

    @Test
    fun `test that the second species is returned if the base species is the first in the chain`() {
        useCase(baseSpeciesId = 1, chainId = 1)
            .test()
            .assertValue(2)
            .dispose()
    }

    @Test
    fun `test that the third species is returned if the base species is the second in the chain`() {
        useCase(baseSpeciesId = 2, chainId = 1)
            .test()
            .assertValue(3)
            .dispose()
    }

    @Test
    fun `test that -1 is returned if the base species does not evolve`() {
        useCase(baseSpeciesId = 3, chainId = 1)
            .test()
            .assertValue(-1)
            .dispose()
    }
}