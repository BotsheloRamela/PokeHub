package com.botsheloramela.pokehub.ui.pokemonList

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.botsheloramela.pokehub.data.model.Pokemon
import com.botsheloramela.pokehub.data.model.PokemonItemModel
import com.botsheloramela.pokehub.data.repository.PokemonRepository
import com.botsheloramela.pokehub.util.Constants.PAGE_SIZE
import com.botsheloramela.pokehub.util.Constants.POKEMON_IMAGE_URL_BASE
import com.botsheloramela.pokehub.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private var currentPage = 0

    var pokemonList = mutableStateOf<List<PokemonItemModel>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    private var cachedPokemonList = emptyList<PokemonItemModel>()
    private var isSearchStarting = true
    val isSearching = mutableStateOf(false)

    init {
        loadPokemonPaginated()
    }

    fun searchPokemonList(query: String) {
        viewModelScope.launch(Dispatchers.Default) {
            // If the query is empty, reset the search and return to the original Pokemon list.
            if (query.isEmpty()) {
                resetSearch()
                return@launch
            }

            // Determine which list to search based on whether the search has just started or not.
            val listToSearch = if (isSearchStarting) {
                pokemonList.value // If search has just started, use the original Pokemon list.
            } else {
                cachedPokemonList // If not, use the previously cached list.
            }

            // Filter the list based on the search query.
            val results = listToSearch.filter {
                it.name.contains(query.trim(), ignoreCase = true) || it.id.toString() == query.trim()
            }

            // If the search has just started, cache the original Pokemon list.
            if (isSearchStarting) cachePokemonList()

            // Update the Pokemon list with the search results.
            updatePokemonList(results)
        }
    }

    /**
     * Resets the search to the initial state.
     * Restores the original Pokemon list and sets [isSearching] to false.
     */
    private fun resetSearch() {
        pokemonList.value = cachedPokemonList
        isSearching.value = false
        isSearchStarting = true
    }

    /**
     * Caches the current Pokemon list.
     * Sets [isSearchStarting] to false, indicating that the search has started.
     */
    private fun cachePokemonList() {
        cachedPokemonList = pokemonList.value
        isSearchStarting = false
    }

    /**
     * Updates the Pokemon list with the provided results.
     * Sets [pokemonList] to the filtered results and [isSearching] to true.
     * @param results The filtered list of Pokemon based on the search query.
     */
    private fun updatePokemonList(results: List<PokemonItemModel>) {
        pokemonList.value = results
        isSearching.value = true
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

    private fun darkenColor(color: Color): Color {
        val hsv = FloatArray(3)
        android.graphics.Color.colorToHSV(color.toArgb(), hsv)

        // Reduce brightness by the specified percentage
        hsv[2] *= (1f - 0.15f)

        return Color(android.graphics.Color.HSVToColor(hsv))
    }

    fun calculateDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                val color = Color(colorValue)
                val darkenedColor = darkenColor(color)
                onFinish(darkenedColor)
            }
        }
    }
}