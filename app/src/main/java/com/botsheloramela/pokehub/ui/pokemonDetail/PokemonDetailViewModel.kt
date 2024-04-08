package com.botsheloramela.pokehub.ui.pokemonDetail

import androidx.lifecycle.ViewModel
import com.botsheloramela.pokehub.data.model.Pokemon
import com.botsheloramela.pokehub.data.model.PokemonSpecies
import com.botsheloramela.pokehub.data.repository.PokemonRepository
import com.botsheloramela.pokehub.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    suspend fun getPokemonInfo(pokemonName: String): Resource<Pokemon> {
        return repository.getPokemonInfo(pokemonName)
    }

    suspend fun getPokemonSpeciesInfo(pokemonName: String): Resource<PokemonSpecies> {
        return repository.getPokemonSpeciesInfo(pokemonName)
    }
}