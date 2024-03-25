package com.botsheloramela.pokehub.ui.pokeList

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.palette.graphics.Palette
import com.botsheloramela.pokehub.data.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokeListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    fun calculateDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bmp = (drawable as? BitmapDrawable)?.bitmap
        bmp?.let { bitmap ->
            Palette.from(bitmap).generate { palette ->
                palette?.dominantSwatch?.rgb?.let { colorValue ->
                    onFinish(Color(colorValue))
                }
            }
        } ?: onFinish(Color.White) // Default color if bitmap is null

        bmp?.recycle() // Recycle the bitmap to release memory
    }
}