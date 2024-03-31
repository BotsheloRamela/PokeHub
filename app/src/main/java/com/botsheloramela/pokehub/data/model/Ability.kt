package com.botsheloramela.pokehub.data.model

data class Ability(
    val ability: AbilityInfo,
    val is_hidden: Boolean,
    val slot: Int
)

data class AbilityInfo(
    val name: String,
    val url: String
)