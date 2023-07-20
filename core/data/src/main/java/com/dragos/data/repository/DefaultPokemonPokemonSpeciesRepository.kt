package com.dragos.data.repository

import com.dragos.data.api.PokemonSpeciesApi
import com.dragos.domain.model.NamedAPIResourceList
import com.dragos.domain.model.PokemonSpecies
import com.dragos.domain.repository.PokemonSpeciesRepository
import io.reactivex.Single
import javax.inject.Inject

class DefaultPokemonPokemonSpeciesRepository @Inject constructor(
    private val apiService: PokemonSpeciesApi
) : PokemonSpeciesRepository {
    override fun getPokemonSpecies(offset: Int): Single<NamedAPIResourceList> {
        return apiService.getPokemonSpecies(offset)
    }

    override fun getPokemonSpeciesById(id: Int): Single<PokemonSpecies> {
        return apiService.getPokemonSpeciesById(id)
    }
}