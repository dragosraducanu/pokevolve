package com.dragos.domain.usecase

import android.annotation.SuppressLint
import com.dragos.domain.model.EvolutionChain
import com.dragos.domain.repository.EvolutionChainRepository
import com.dragos.domain.util.UrlUtils
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GetNextSpeciesInEvolutionChainUseCase @Inject constructor(
    private val evolutionChainRepository: EvolutionChainRepository
) {
    @SuppressLint("CheckResult")
    operator fun invoke(baseSpeciesId: Int, chainId: Int): Single<Int> =
        Single.create { emitter ->
            evolutionChainRepository.getEvolutionChain(chainId)
                .subscribeOn(Schedulers.io())
                .subscribe({ response ->
                    emitter.onSuccess(findNextSpeciesInChain(baseSpeciesId, response))
                }, { throwable ->
                    emitter.onError(throwable)
                })
        }

    private fun findNextSpeciesInChain(baseSpeciesId: Int, evolutionChain: EvolutionChain): Int {
        var currentChain = evolutionChain.chain
        while (currentChain.evolvesTo.isNotEmpty()
            && UrlUtils.getIdFromUrl(currentChain.species.url) != baseSpeciesId
        ) {
            currentChain = currentChain.evolvesTo.first()
        }

        return try {
            UrlUtils.getIdFromUrl(currentChain.evolvesTo.first().species.url)
        } catch (e: NoSuchElementException) {
            -1
        } ?: -1
    }
}
