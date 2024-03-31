package com.botsheloramela.pokehub.data.model

data class Sprites(
    val back_default: String,
    val back_female: Any,
    val back_shiny: String,
    val back_shiny_female: Any,
    val front_default: String,
    val front_female: Any,
    val front_shiny: String,
    val front_shiny_female: Any,
    val other: Other,
//    val versions: Versions
)

data class Other(
    val home: Home,
//    val dreamWorld: DreamWorld,
//    val officialArtwork: OfficialArtwork,
//    val showdown: Showdown
)

data class Home(
    val front_default: String,
    val front_female: Any,
    val front_shiny: String,
    val front_shiny_female: Any
)