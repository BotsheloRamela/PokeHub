package com.botsheloramela.pokehub.ui.pokemonDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.botsheloramela.pokehub.R
import com.botsheloramela.pokehub.data.model.Pokemon
import com.botsheloramela.pokehub.data.model.PokemonSpecies
import com.botsheloramela.pokehub.ui.pokemonDetail.components.PokemonDetailAppBar
import com.botsheloramela.pokehub.ui.pokemonDetail.components.PokemonDetailSection
import com.botsheloramela.pokehub.ui.pokemonDetail.components.PokemonSummarySection
import com.botsheloramela.pokehub.util.Resource

@Composable
fun PokemonDetailScreen(
    dominantColor: Color,
    pokemonName: String,
    navController: NavController,
    topPadding: Dp = 20.dp,
    pokemonImageSize: Dp = 200.dp,
    viewModel: PokemonDetailViewModel = hiltViewModel()
) {
    val pokemonInfo = produceState<Resource<Pokemon>>(initialValue = Resource.Loading()) {
        value = viewModel.getPokemonInfo(pokemonName)
    }.value

    val pokemonSpecies = produceState<Resource<PokemonSpecies>>(initialValue = Resource.Loading()) {
        value = viewModel.getPokemonSpeciesInfo(pokemonName)
    }.value

    Box(modifier = Modifier
        .fillMaxSize()
        .background(dominantColor)
    ) {
        PokemonDetailAppBar(
            navController = navController
        )

        PokemonDetailStateWrapper(
            pokemonInfo = pokemonInfo,
            pokemonSpecies = pokemonSpecies,
            dominantColor = dominantColor,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topPadding + pokemonImageSize / 0.6f)
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .background(MaterialTheme.colorScheme.surface)
                .align(Alignment.BottomCenter),
            loadingModifier = Modifier
                .size(100.dp)
                .align(Alignment.Center)
                .padding(
                    top = topPadding + pokemonImageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
        )

        Box(contentAlignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
        ) {
            if (pokemonInfo is Resource.Success && pokemonSpecies is Resource.Success) {
                pokemonInfo.data.sprites.other.home.let {
                    Box(
                        modifier = Modifier
                            .offset(y = 145.dp, x = 60.dp)
                            .rotate(20f)
                            .zIndex(0f)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.pokedex_logo),
                            contentDescription = null,
                            modifier = Modifier
                                .size(250.dp)
                                .aspectRatio(1.6f)
                                .alpha(0.3f)
                        )
                    }
                    PokemonSummarySection(
                        pokemonName = pokemonInfo.data.name,
                        pokemonId = pokemonInfo.data.id,
                        pokemonTypes = pokemonInfo.data.types,
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = 70.dp)
                            .zIndex(2f)
                    )

                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(it.front_default)
                            .crossfade(true)
                            .build(),
                        contentDescription = pokemonInfo.data.name,
                        modifier = Modifier
                            .size(pokemonImageSize)
                            .offset(y = 180.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PokemonDetailStateWrapper(
    pokemonInfo: Resource<Pokemon>,
    pokemonSpecies: Resource<PokemonSpecies>,
    dominantColor: Color,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier,
) {

    var pokemonFlavourText by remember {
        mutableStateOf("Loading...")
    }

    var isFlavorTextLoading by remember {
        mutableStateOf(true)
    }

    when (pokemonInfo) {
        is Resource.Success -> {

            if (pokemonSpecies is Resource.Success && isFlavorTextLoading) {
                val flavorTextEntries = pokemonSpecies.data.flavor_text_entries.filter { it.language.name == "en" }
                pokemonFlavourText = flavorTextEntries.randomOrNull()
                    ?.flavor_text?.replace("\n", " ") ?: "No description"
                isFlavorTextLoading = false
            }

            if(!isFlavorTextLoading) {
                PokemonDetailSection(
                    pokemonInfo = pokemonInfo.data,
                    pokemonFlavourText = pokemonFlavourText,
                    dominantColor = dominantColor,
                    modifier = modifier
                )
            }
        }
        is Resource.Error -> {
            Text(
                text = "An error occurred",
                color = Color.Red,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier
            )
        }
        is Resource.Loading -> {
            CircularProgressIndicator(
                color = dominantColor,
                modifier = loadingModifier
            )
        }
    }
}