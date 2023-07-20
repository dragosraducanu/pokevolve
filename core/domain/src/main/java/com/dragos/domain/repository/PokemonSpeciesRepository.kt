package com.dragos.domain.repository

import com.dragos.domain.model.NamedAPIResourceList
import com.dragos.domain.model.PokemonSpecies
import io.reactivex.Single

interface PokemonSpeciesRepository {
    fun getPokemonSpecies(offset: Int): Single<NamedAPIResourceList>
    fun getPokemonSpeciesById(id: Int): Single<PokemonSpecies>
}