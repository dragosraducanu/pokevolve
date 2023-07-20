package com.dragos.data.api

import com.dragos.domain.model.NamedAPIResourceList
import com.dragos.domain.model.PokemonSpecies
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonSpeciesApi {
    @GET("pokemon-species")
    fun getPokemonSpecies(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = 20
    ): Single<NamedAPIResourceList>

    @GET("pokemon-species/{id}")
    fun getPokemonSpeciesById(
        @Path("id") id: Int
    ): Single<PokemonSpecies>
}