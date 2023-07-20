package com.dragos.domain.usecase

import android.annotation.SuppressLint
import com.dragos.domain.model.PokemonSpecies
import com.dragos.domain.repository.PokemonSpeciesRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GetPokemonSpeciesByIdUseCase @Inject constructor(
    private val pokemonSpeciesRepository: PokemonSpeciesRepository
){
    @SuppressLint("CheckResult")
    operator fun invoke(id: Int): Single<PokemonSpecies> =
            Single.create { emitter ->
                pokemonSpeciesRepository.getPokemonSpeciesById(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response ->
                        emitter.onSuccess(response)
                    }, { throwable ->
                        emitter.onError(throwable)
                    })
            }
}