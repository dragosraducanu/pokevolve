package com.dragos.domain.model

import com.google.gson.annotations.SerializedName

data class PokemonSpecies(
    @SerializedName("capture_rate")
    val captureRate: Int,

    @SerializedName("flavor_text_entries")
    val flavorTextEntries: List<FlavorTextEntry>,

    @SerializedName("name")
    val name: String,

    @SerializedName("evolution_chain")
    val evolutionChain: APIResource
)

data class FlavorTextEntry(
    @SerializedName("flavor_text")
    val flavorText: String,

    @SerializedName("language")
    val language: NamedAPIResource
)