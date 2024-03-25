package com.botsheloramela.pokehub.ui.pokeList

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.botsheloramela.pokehub.data.model.PokemonItemModel
import com.botsheloramela.pokehub.data.repository.PokemonRepository
import com.botsheloramela.pokehub.util.Constants.PAGE_SIZE
import com.botsheloramela.pokehub.util.Constants.POKEMON_IMAGE_URL_BASE
import com.botsheloramela.pokehub.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PokeListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private var currentPage = 0

    var pokemonList = mutableStateOf<List<PokemonItemModel>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    init {
        loadPokemonPaginated()
    }

    fun loadPokemonPaginated() {
        viewModelScope.launch {
            try {
                isLoading.value = true
                when (val result = repository.getPokemonList(PAGE_SIZE, currentPage * PAGE_SIZE)) {
                    is Resource.Success -> {
                        endReached.value = currentPage * PAGE_SIZE >= result.data.count
                        val pagedPokemonList = result.data.results.map { pokemon ->
                            val number = pokemon.url.split("/").last { it.isNotEmpty() }.toInt()
                            val imageUrl = "${POKEMON_IMAGE_URL_BASE}$number.png"
                            PokemonItemModel(
                                name = pokemon.name.replaceFirstChar { it.uppercase() },
                                id = number,
                                imageUrl = imageUrl
                            )
                        }
                        currentPage++
                        loadError.value = ""
                        pokemonList.value += pagedPokemonList
                    }
                    is Resource.Error -> {
                        loadError.value = result.message
                    }
                    else -> {
                        loadError.value = "An unexpected error occurred."
                    }
                }
            } catch (e: Exception) {
                loadError.value = "Error: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun calculateDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }
}