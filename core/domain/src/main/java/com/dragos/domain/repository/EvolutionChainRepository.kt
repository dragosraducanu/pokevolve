package com.dragos.domain.repository

import com.dragos.domain.model.EvolutionChain
import io.reactivex.Single

interface EvolutionChainRepository {
    fun getEvolutionChain(id: Int): Single<EvolutionChain>
}