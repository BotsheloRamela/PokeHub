package com.botsheloramela.pokehub.data.repository

import com.botsheloramela.pokehub.data.model.Pokemon
import com.botsheloramela.pokehub.data.model.PokemonList
import com.botsheloramela.pokehub.data.model.PokemonSpecies
import com.botsheloramela.pokehub.data.remote.PokeApi
import com.botsheloramela.pokehub.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PokemonRepository @Inject constructor(
    private val api: PokeApi
) {
    suspend fun getPokemonList(limit: Int, offset: Int): Resource<PokemonList> {
        return try {
            val response = api.getPokemonList(limit, offset)
            Resource.Success(response)
        } catch (e: Exception) {
            Resource.Error("Failed to fetch Pokemon list: ${e.message}")
        }
    }

    suspend fun getPokemonInfo(pokemonName: String): Resource<Pokemon> {
        return try {
            val response = api.getPokemonInfo(pokemonName)
            Resource.Success(response)
        } catch (e: Exception) {
            Resource.Error("Failed to fetch Pokemon info: ${e.message}")
        }
    }

    suspend fun getPokemonSpeciesInfo(pokemonName: String): Resource<PokemonSpecies> {
        return try {
            val response = api.getPokemonSpeciesInfo(pokemonName)
            Resource.Success(response)
        } catch (e: Exception) {
            Resource.Error("Failed to fetch Pokemon species info: ${e.message}")
        }
    }
}