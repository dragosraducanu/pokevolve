package com.dragos.data.di

import com.dragos.data.api.EvolutionChainApi
import com.dragos.data.api.PokemonSpeciesApi
import com.dragos.data.repository.DefaultEvolutionChainRepository
import com.dragos.data.repository.DefaultPokemonPokemonSpeciesRepository
import com.dragos.domain.repository.EvolutionChainRepository
import com.dragos.domain.repository.PokemonSpeciesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun providesPokemonSpeciesRepository(
        pokemonSpeciesApi: PokemonSpeciesApi
    ): PokemonSpeciesRepository = DefaultPokemonPokemonSpeciesRepository(pokemonSpeciesApi)

    @Singleton
    @Provides
    fun providesEvolutionChainRepository(
        evolutionChainApi: EvolutionChainApi
    ): EvolutionChainRepository = DefaultEvolutionChainRepository(evolutionChainApi)
}