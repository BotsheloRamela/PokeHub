package com.botsheloramela.pokehub.data.model

data class Move(
    val name: String,
    val learnMethod: String,
    val versionGroup: VersionGroup
)

data class VersionGroup(
    val name: String,
    val url: String
)