package com.dragos.domain.usecase

import android.annotation.SuppressLint
import com.dragos.domain.model.NamedAPIResourceList
import com.dragos.domain.repository.PokemonSpeciesRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GetPokemonSpeciesListUseCase @Inject constructor(
    private val pokemonSpeciesRepository: PokemonSpeciesRepository
){
    @SuppressLint("CheckResult")
    operator fun invoke(offset: Int): Single<NamedAPIResourceList> =
            Single.create { emitter ->
                pokemonSpeciesRepository.getPokemonSpecies(offset)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response ->
                        emitter.onSuccess(response)
                    }, { throwable ->
                        emitter.onError(throwable)
                    })
            }
}