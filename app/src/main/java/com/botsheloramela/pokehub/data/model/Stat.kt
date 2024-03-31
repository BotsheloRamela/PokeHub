package com.botsheloramela.pokehub.data.model

data class Stat(
    val base_stat: Int,
    val effort: Int,
    val stat: StatInfo
)

data class StatInfo(
    val name: String,
    val url: String
)