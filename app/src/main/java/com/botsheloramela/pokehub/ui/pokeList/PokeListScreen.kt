package com.botsheloramela.pokehub.ui.pokeList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun PokeListScreen(
    navController: NavController
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column (
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)
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
                // viewModel.searchPokemonList(it)
            }
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
                .padding(horizontal = 20.dp,vertical = 14.dp)
                .onFocusChanged {
                    isHintDisplayed = !it.isFocused
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
}


//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    PokeListScreen()
//}


