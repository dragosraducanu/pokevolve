package com.dragos.pokevolve.main

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dragos.pokevolve.R
import com.dragos.pokevolve.ui.PokeAppBar
import com.dragos.pokevolve.ui.theme.dimen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    val viewModel: MainViewModel = hiltViewModel()

    LaunchedEffect(true) {
        viewModel.loadPokemonSpecies()
    }

    Scaffold(modifier = Modifier, topBar = {
        PokeAppBar(
            modifier = Modifier.padding(vertical = MaterialTheme.dimen.paddingLarge),
            navController = navController
        )
    }) {

        when {
            viewModel.screenState.value.isLoading -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator()
                }
            }

            viewModel.screenState.value.pokemonSpeciesList.isEmpty() -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = stringResource(id = R.string.species_list_empty),
                        style = MaterialTheme.typography.titleMedium,
                    )

                    Spacer(modifier = Modifier.height(MaterialTheme.dimen.paddingSmall))

                    Button(onClick = { viewModel.loadPokemonSpecies() }) {
                        Text(
                            text = "Try again"
                        )
                    }
                }
            }

            else -> PokemonSpeciesList(
                modifier = Modifier
                    .padding(top = it.calculateTopPadding())
                    .padding(
                        horizontal = MaterialTheme.dimen.paddingLarge,
                    ),
                pokemonSpecies = viewModel.screenState.value.pokemonSpeciesList,
                isLoadingNextPage = viewModel.screenState.value.isLoadingNextPage,
                onLoadNextPage = {
                    viewModel.loadPokemonSpecies()
                },
                navController = navController
            )
        }

        if (viewModel.screenState.value.error &&
            viewModel.screenState.value.pokemonSpeciesList.isNotEmpty()
        ) {
            val localContext = LocalContext.current
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Toast.makeText(
                    localContext,
                    stringResource(id = R.string.species_list_next_page_error_message),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}