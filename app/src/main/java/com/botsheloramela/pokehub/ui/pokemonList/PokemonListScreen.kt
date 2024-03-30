package com.botsheloramela.pokehub.ui.pokemonList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.botsheloramela.pokehub.data.model.PokemonItemModel


@Composable
fun PokeListScreen(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ){
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "PokeHub",
                style = TextStyle(color = MaterialTheme.colorScheme.primary, fontSize = 30.sp, fontWeight = FontWeight.SemiBold)
            )

            Text(
                text = "Search for a Pokemon by name or using the National PokeHub number",
                style = TextStyle(color = Color.Gray, fontSize = 14.sp, fontWeight = FontWeight.Light),
                modifier = Modifier.padding(vertical = 12.dp)
            )

            SearchBar(
                hint = "Search...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                 viewModel.searchPokemonList(it)
            }
            Spacer(modifier = Modifier.height(16.dp))
            PokemonList(navController = navController)
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {}
) {
    var searchText by remember {
        mutableStateOf("")
    }

    var isHintDisplayed by remember {
        mutableStateOf(hint.isNotEmpty())
    }

    Box(modifier = modifier) {
        // TODO: Add search icon
        BasicTextField(
            value = searchText,
            maxLines = 1,
            singleLine = true,
            onValueChange = {
                searchText = it
                onSearch(it)
            },
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onPrimary, CircleShape)
                .padding(horizontal = 20.dp, vertical = 14.dp)
                .onFocusChanged {
                    isHintDisplayed = !it.isFocused && searchText.isEmpty()
                }
        )

        if (isHintDisplayed) {
            Text(
                text = hint,
                style = TextStyle(color = Color.Gray.copy(alpha = 0.5f)),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)
            )
        }
    }
    // TODO: Add filter icon / button
}

@Composable
fun PokemonList(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val pokemonList by remember { viewModel.pokemonList }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }
    val endReached by remember { viewModel.endReached }
    val isSearching by remember { viewModel.isSearching }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        if (loadError.isNotEmpty()) {
            ErrorRetrySection(error = loadError) {
                viewModel.loadPokemonPaginated()
            }
        }
        if (pokemonList.isNotEmpty()) {
            DisplayPokemonGrid(
                pokemonList = pokemonList,
                navController = navController,
                modifier = Modifier.fillMaxSize(),
                onLoadMore = { viewModel.loadPokemonPaginated() },
                endReached = endReached,
                isLoading = isLoading,
                isSearching = isSearching
            )
        }
        if (endReached) {
            Text(
                text = "You have reached the end of the list",
                color = Color.Gray,
                fontSize = 20.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}


@Composable
fun DisplayPokemonItem(
    pokemonItem: PokemonItemModel,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = hiltViewModel()
) {

    val defaultDominantColor = MaterialTheme.colorScheme.surface
    var dominantColor by remember { mutableStateOf(defaultDominantColor) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .background(dominantColor)
            .clickable {
                navController.navigate("pokemon_details_screen/${dominantColor.toArgb()}/${pokemonItem.name}")
            }
    ) {
        Column {

            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(pokemonItem.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = pokemonItem.name,
                loading = {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary, modifier = Modifier.scale(0.5F)
                    )
                },
                success = { success ->
                    viewModel.calculateDominantColor(success.result.drawable) { dominantColor = it }
                    SubcomposeAsyncImageContent()
                },
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = pokemonItem.name,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun DisplayPokemonGrid(
    pokemonList: List<PokemonItemModel>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onLoadMore: () -> Unit,
    endReached: Boolean,
    isLoading: Boolean,
    isSearching: Boolean
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(4.dp),
    ) {
        items(pokemonList) { pokemon ->
            DisplayPokemonItem(
                pokemonItem = pokemon,
                navController = navController,
                modifier = modifier.padding(8.dp)
            )
            // If we're at the last item and there are more to load, trigger onLoadMore
            if (pokemon == pokemonList.last() && !endReached && !isLoading && !isSearching) onLoadMore()
        }
    }
}

@Composable
fun ErrorRetrySection(
    error: String,
    onRetry: () -> Unit
) {
    Column {
        Text(
            text = error,
            color = Color.Red,
            fontSize = 20.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Button(
            onClick = { onRetry() },
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Retry")
        }
    }

}


//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    PokeListScreen()
//}


