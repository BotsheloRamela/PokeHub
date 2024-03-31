package com.botsheloramela.pokehub.data.model

data class Pokemon(
    val id: Int,
    val name: String,
    val baseExperience: Int,
    val height: Int,
    val weight: Int,
    val abilities: List<Ability>,
    val moves: List<Move>,
    val sprites: Sprites,
    val stats: List<Stat>,
    val types: List<Type>,
    val imageUrl : String
)

data class PokemonItemModel(
    val name: String,
    val id: Int,
    val imageUrl: String
)

data class PokemonList(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Result>
)
