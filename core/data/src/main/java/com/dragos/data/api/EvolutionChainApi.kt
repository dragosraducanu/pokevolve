package com.dragos.data.api

import com.dragos.domain.model.EvolutionChain
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface EvolutionChainApi {
    @GET("evolution-chain/{id}")
    fun getEvolutionChain(
        @Path("id") id: Int
    ): Single<EvolutionChain>
}