package com.dragos.pokevolve

import com.dragos.pokevolve.util.ImageUtils
import org.junit.Assert
import org.junit.Test

class ImageUtilsUnitTest {
    @Test
    fun `Species Image URL is generated for a valid id`() {
        Assert.assertEquals(
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/5.png",
            ImageUtils.getPokemonSpeciesImageUrlOrDefault(5)
        )
    }

    @Test
    fun `Default URL is returned for a null id`() {
        Assert.assertEquals(
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png",
            ImageUtils.getPokemonSpeciesImageUrlOrDefault(null)
        )
    }
}