package com.botsheloramela.pokehub.ui.pokemonDetail

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.produceState
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.botsheloramela.pokehub.data.model.Pokemon
import com.botsheloramela.pokehub.data.model.PokemonSpecies
import com.botsheloramela.pokehub.data.model.Type
import com.botsheloramela.pokehub.util.Resource
import com.botsheloramela.pokehub.util.parseStatToAbbr
import com.botsheloramela.pokehub.util.parseTypeToColor
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.round

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
                    PokemonSummarySection(
                        pokemonName = pokemonInfo.data.name,
                        pokemonId = pokemonInfo.data.id,
                        pokemonTypes = pokemonInfo.data.types,
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = 70.dp)
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
fun PokemonSummarySection(
    pokemonName: String,
    pokemonId: Int,
    pokemonTypes: List<Type>,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        Box(modifier = Modifier.weight(1f)) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .wrapContentWidth(Alignment.Start)
            ) {
                Text(
                    text = pokemonName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
                PokemonTypeSection(types = pokemonTypes)
            }
        }
        
        Text(
            text = "#${String.format("%03d", pokemonId)}",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Right,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun PokemonTypeSection(types: List<Type>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        types.forEach { type ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(color = parseTypeToColor(type), shape = RoundedCornerShape(30.dp))
                    .height(35.dp)
            ) {
                Text(
                    text = type.type.name.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.ROOT
                        ) else it.toString()
                    },
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailAppBar(
    navController: NavController,
) {
    CenterAlignedTopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        ),
        title = {Text( text = "")},
        navigationIcon = {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )
        },
    )
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PokemonDetailSection(
    pokemonInfo: Pokemon,
    pokemonFlavourText: String,
    dominantColor: Color,
    modifier: Modifier
) {
    val tabItems = listOf("Info") // TODO: Add "Evolutions" and "Moves" tabs

    val pagerState = rememberPagerState(
        pageCount = { tabItems.size }
    )

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 28.dp)
    ) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier.clip(RoundedCornerShape(16.dp)),
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier
                        .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                        .width(0.dp)
                        .height(0.dp)
                )
            },
            divider = { },
        ) {
            tabItems.forEachIndexed { index, title ->
                val tabColor = remember {
                    Animatable(Color.White)
                }

                LaunchedEffect(key1 = pagerState.currentPage == index) {
                    tabColor.animateTo(
                        if (pagerState.currentPage == index) dominantColor else Color.Transparent
                    )
                }

                Tab(
                    text = {
                        Text(
                            title,
                            color = if (pagerState.currentPage == index) Color.White else Color.Gray,
                            modifier = Modifier
                                .background(
                                    color = tabColor.value, shape = RoundedCornerShape(30.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                       },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)

        ) {page ->
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                if (page == 0) {
                    PokemonDetailDataSection(
                        pokemonWeight = pokemonInfo.weight,
                        pokemonHeight = pokemonInfo.height,
                        dominantColor = dominantColor
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = pokemonFlavourText,
                        color = Color.Gray,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    PokemonBaseStats(pokemonInfo = pokemonInfo, dominantColor = dominantColor)
                }
            }
        }
    }
}

@Composable
fun PokemonDetailDataSection(
    pokemonWeight: Int,
    pokemonHeight: Int,
    dominantColor: Color
) {

    val pokemonWeightInKg = remember {
        round(pokemonWeight * 100f) / 1000f
    }

    val pokemonHeightInMeters = remember {
        round(pokemonHeight * 100f) / 1000f
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        PokemonDetailDataItem(
            dataTitle = "Height",
            dataValue = "${pokemonHeightInMeters}m",
            dominantColor = dominantColor
        )
        PokemonDetailDataItem(
            dataTitle = "Weight",
            dataValue = "${pokemonWeightInKg}kg",
            dominantColor = dominantColor
        )
    }
}

@Composable
fun PokemonDetailDataItem(
    dataTitle: String,
    dataValue: String,
    dominantColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(
            text = dataTitle,
            color = Color.Gray,
            fontSize = 12.sp
        )
        Text(
            text = dataValue,
            color = dominantColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun PokemonStat(
    statName: String,
    statValue: Int,
    statMaxValue: Int,
    dominantColor: Color,
    animationDuration: Int = 1000,
    animationDelay: Int = 0
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }

    val currentPercent = animateFloatAsState(
        targetValue = if (animationPlayed) {
            (statValue / statMaxValue.toFloat()).coerceIn(0f, 1f)
        } else 0f,
        label = "",
        animationSpec = tween(
            durationMillis = animationDuration,
            delayMillis = animationDelay
        )
    )

    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = statName,
                color = Color.Gray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(54.dp)
            )
            Text(
                text = (currentPercent.value * statMaxValue).toInt().toString(),
                color = Color.LightGray,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.width(28.dp)
            )
        }

        Box(
            modifier = Modifier
                .height(12.dp)
                .fillMaxWidth()
                .background(
                    color = Color.LightGray.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Box (
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(currentPercent.value * statMaxValue / 100)
                    .clip(RoundedCornerShape(16.dp))
                    .background(dominantColor)
            )
        }
    }
}

@Composable
fun PokemonBaseStats(
    pokemonInfo: Pokemon,
    animationDelayPerItem: Int = 100,
    dominantColor: Color
) {
    val maxBaseState = remember {
        pokemonInfo.stats.maxOf { it.base_stat }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        pokemonInfo.stats.forEachIndexed { index, stat ->
            PokemonStat(
                statName = parseStatToAbbr(stat),
                statValue = stat.base_stat,
                statMaxValue = maxBaseState,
                dominantColor = dominantColor,
                animationDelay = index * animationDelayPerItem,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}