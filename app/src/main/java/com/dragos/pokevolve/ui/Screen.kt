package com.dragos.pokevolve.ui

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object SpeciesDetailScreen : Screen("species_details_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {
                append("/$it")
            }
        }
    }
}