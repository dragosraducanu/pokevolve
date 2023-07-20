package com.dragos.pokevolve.util

object ImageUtils {
    fun getPokemonSpeciesImageUrlOrDefault(id: Int?) =
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${id ?: 1}.png"
}