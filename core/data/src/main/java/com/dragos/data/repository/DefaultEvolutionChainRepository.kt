package com.dragos.data.repository

import com.dragos.data.api.EvolutionChainApi
import com.dragos.domain.model.EvolutionChain
import com.dragos.domain.repository.EvolutionChainRepository
import io.reactivex.Single
import javax.inject.Inject

class DefaultEvolutionChainRepository @Inject constructor(
    private val apiService: EvolutionChainApi
) : EvolutionChainRepository {
    override fun getEvolutionChain(id: Int): Single<EvolutionChain> {
        return apiService.getEvolutionChain(id)
    }
}