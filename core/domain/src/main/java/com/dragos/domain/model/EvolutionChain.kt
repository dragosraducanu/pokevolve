package com.dragos.domain.model

import com.google.gson.annotations.SerializedName

data class EvolutionChain(
    @SerializedName("chain")
    val chain: ChainLink
)

data class ChainLink(
    @SerializedName("evolves_to")
    val evolvesTo: List<ChainLink>,

    @SerializedName("species")
    val species: NamedAPIResource
)