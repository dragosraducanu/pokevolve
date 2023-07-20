package com.dragos.pokevolve.main

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.dragos.domain.model.NamedAPIResource
import com.dragos.domain.model.id
import com.dragos.pokevolve.R
import com.dragos.pokevolve.ui.Screen
import com.dragos.pokevolve.ui.theme.dimen
import com.dragos.pokevolve.util.ImageUtils
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PokemonSpeciesList(
    modifier: Modifier = Modifier,
    pokemonSpecies: List<NamedAPIResource>,
    isLoadingNextPage: Boolean,
    onLoadNextPage: () -> Unit,
    navController: NavController
) {

    val lazyListState = rememberLazyListState()

    LazyColumn(
        state = lazyListState,
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.paddingMedium)
    ) {
        items(pokemonSpecies) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(Screen.SpeciesDetailScreen.withArgs(it.id.toString()))
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlideImage(
                    model = ImageUtils.getPokemonSpeciesImageUrlOrDefault(it.id),
                    contentDescription = stringResource(R.string.content_desc_pokemon_species_image),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(65.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.onBackground,
                            shape = RoundedCornerShape(10.dp)
                        )
                )
                Spacer(modifier = Modifier.width(MaterialTheme.dimen.paddingMedium))

                Text(
                    text = it.name.capitalize(Locale.current),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }

        item {
            if (isLoadingNextPage) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = MaterialTheme.dimen.paddingSmall),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }

    PaginationStateHandler(state = lazyListState) {
        onLoadNextPage()
    }
}

@Composable
fun PaginationStateHandler(
    state: LazyListState,
    buffer: Int = 2,
    onLoadNextPage: () -> Unit
) {

    val loadNext = remember {
        derivedStateOf {
            val layoutInfo = state.layoutInfo
            val totalItemsCount = layoutInfo.totalItemsCount
            val lastVisibleItemIndex =
                (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1

            lastVisibleItemIndex > (totalItemsCount - buffer)
        }
    }

    LaunchedEffect(loadNext) {
        snapshotFlow { loadNext.value }
            .distinctUntilChanged()
            .collect {
                if (it) {
                    onLoadNextPage()
                }
            }
    }
}