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
    val imageUrl: String,
    val species: PokemonSpecies
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

data class PokemonSpecies(
    val flavor_text_entries: List<FlavorTextEntry>
)

data class FlavorTextEntry(
    val flavor_text: String,
    val language: Language
)

data class Language(
    val name: String,
    val url: String
)
