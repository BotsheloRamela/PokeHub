package com.botsheloramela.pokehub.data.remote

import com.botsheloramela.pokehub.data.model.Pokemon
import com.botsheloramela.pokehub.data.model.PokemonList
import com.botsheloramela.pokehub.data.model.PokemonSpecies
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApi {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonList

    @GET("pokemon/{name}")
    suspend fun getPokemonInfo(
        @Path("name") name: String
    ): Pokemon

    @GET("pokemon-species/{name}")
    suspend fun getPokemonSpeciesInfo(
        @Path("name") name: String
    ): PokemonSpecies

}