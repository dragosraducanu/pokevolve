package com.dragos.pokevolve.details

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.dragos.pokevolve.R
import com.dragos.pokevolve.ui.PokeAppBar
import com.dragos.pokevolve.ui.Screen
import com.dragos.pokevolve.ui.theme.dimen
import com.dragos.pokevolve.util.ImageUtils
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SpeciesDetailScreen(id: Int, navController: NavController) {

    val viewModel: PokemonSpeciesDetailsViewModel = hiltViewModel()

    LaunchedEffect(true) {
        viewModel.loadPokemonSpecies(id)
    }

    val screenState = viewModel.screenState.value

    Scaffold(modifier = Modifier, topBar = {
        PokeAppBar(
            modifier = Modifier.padding(vertical = MaterialTheme.dimen.paddingLarge),
            navController = navController
        )
    }) {
        if (screenState.errorEvolutionSpecies) {
            Toast.makeText(
                LocalContext.current,
                stringResource(id = R.string.species_details_evolution_not_found),
                Toast.LENGTH_SHORT
            ).show()
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = it.calculateTopPadding(),
                )
                .padding(horizontal = MaterialTheme.dimen.paddingLarge)
            ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                when {
                    screenState.isLoading -> CircularProgressIndicator()
                    screenState.errorBaseSpecies ||
                            screenState.baseSpecies == null -> {
                        Text(
                            text = stringResource(id = R.string.species_details_not_found),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }

                    else -> PokemonSpeciesCard(
                        id = id,
                        name = screenState.baseSpecies.name.capitalize(Locale.getDefault()),
                        flavorText = screenState.baseSpecies.flavorText
                    )
                }

            }

            item {
                when {
                    screenState.isLoadingEvolutionChain -> {}
                    screenState.doesNotEvolve -> {
                        Text(
                            modifier = Modifier.padding(MaterialTheme.dimen.paddingLarge),
                            text = stringResource(id = R.string.species_details_doesnt_evolve),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                    screenState.evolutionSpecies == null
                            || screenState.captureRateDifference == null -> {}

                    else -> {
                        EvolutionChainArrow(
                            modifier = Modifier.padding(vertical = MaterialTheme.dimen.paddingMedium),
                            captureRateDiff = screenState.captureRateDifference
                        )

                        PokemonSpeciesCard(
                            modifier = Modifier.clickable {
                                navController.navigate(
                                    Screen.SpeciesDetailScreen.withArgs(
                                        screenState.evolutionSpecies.id.toString()
                                    )
                                )
                            },
                            id = screenState.evolutionSpecies.id,
                            name = screenState.evolutionSpecies.name,
                            flavorText = screenState.evolutionSpecies.flavorText
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EvolutionChainArrow(modifier: Modifier = Modifier, captureRateDiff: Int) {

    val lineColor = if (captureRateDiff >= 0) {
        Color.Green
    } else {
        MaterialTheme.colorScheme.error
    }

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.rotate(-90f),
            imageVector = Icons.Filled.ArrowBack,
            tint = lineColor,
            contentDescription = stringResource(id = R.string.content_desc_evolution_arrow)
        )
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(120.dp)
                    .background(lineColor),
                contentAlignment = Alignment.Center
            ) {}
            Text(
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(MaterialTheme.dimen.paddingMedium),
                color = lineColor,
                text = captureRateDiff.toString(),
            )
        }
        Icon(
            modifier = Modifier.rotate(-90f),
            imageVector = Icons.Filled.ArrowBack,
            tint = lineColor,
            contentDescription = stringResource(id = R.string.content_desc_evolution_arrow)
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PokemonSpeciesCard(modifier: Modifier = Modifier, id: Int, name: String, flavorText: String) {

    Card(
        modifier = modifier
            .padding(horizontal = MaterialTheme.dimen.paddingLarge)
            .fillMaxWidth(),

        ) {
        Column(
            modifier = Modifier
                .padding(MaterialTheme.dimen.paddingLarge)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.paddingMedium)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
            )

            GlideImage(
                model = ImageUtils.getPokemonSpeciesImageUrlOrDefault(id),
                contentDescription = stringResource(
                    id = R.string.content_desc_pokemon_species_image
                ),
                modifier = Modifier.size(100.dp)
            )

            if (flavorText.isNotBlank()) {
                Spacer(modifier = Modifier.width(MaterialTheme.dimen.paddingMedium))
                Text(
                    modifier = modifier.fillMaxWidth(),
                    text = flavorText,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }

    }

}