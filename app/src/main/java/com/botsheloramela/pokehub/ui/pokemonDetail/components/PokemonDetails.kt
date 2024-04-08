package com.botsheloramela.pokehub.ui.pokemonDetail.components

import androidx.compose.animation.Animatable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.botsheloramela.pokehub.data.model.Pokemon
import kotlinx.coroutines.launch
import kotlin.math.round

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
            .verticalScroll(rememberScrollState())
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
                    Spacer(modifier = Modifier.height(28.dp))
                    Text(
                        text = pokemonFlavourText,
                        color = Color.Gray,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    PokemonBaseStats(pokemonInfo = pokemonInfo, dominantColor = dominantColor)
                    Spacer(modifier = Modifier.height(12.dp))
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