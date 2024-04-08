package com.botsheloramela.pokehub.util

import androidx.compose.ui.graphics.Color
import com.botsheloramela.pokehub.data.model.Stat
import com.botsheloramela.pokehub.data.model.Type
import com.botsheloramela.pokehub.ui.theme.AtkColor
import com.botsheloramela.pokehub.ui.theme.DefColor
import com.botsheloramela.pokehub.ui.theme.HPColor
import com.botsheloramela.pokehub.ui.theme.SpAtkColor
import com.botsheloramela.pokehub.ui.theme.SpDefColor
import com.botsheloramela.pokehub.ui.theme.SpdColor
import com.botsheloramela.pokehub.ui.theme.TypeBug
import com.botsheloramela.pokehub.ui.theme.TypeDark
import com.botsheloramela.pokehub.ui.theme.TypeDragon
import com.botsheloramela.pokehub.ui.theme.TypeElectric
import com.botsheloramela.pokehub.ui.theme.TypeFairy
import com.botsheloramela.pokehub.ui.theme.TypeFighting
import com.botsheloramela.pokehub.ui.theme.TypeFire
import com.botsheloramela.pokehub.ui.theme.TypeFlying
import com.botsheloramela.pokehub.ui.theme.TypeGhost
import com.botsheloramela.pokehub.ui.theme.TypeGrass
import com.botsheloramela.pokehub.ui.theme.TypeGround
import com.botsheloramela.pokehub.ui.theme.TypeIce
import com.botsheloramela.pokehub.ui.theme.TypeNormal
import com.botsheloramela.pokehub.ui.theme.TypePoison
import com.botsheloramela.pokehub.ui.theme.TypePsychic
import com.botsheloramela.pokehub.ui.theme.TypeRock
import com.botsheloramela.pokehub.ui.theme.TypeSteel
import com.botsheloramela.pokehub.ui.theme.TypeWater
import java.util.Locale

fun parseTypeToColor(type: Type): Color {
    return when(type.type.name.toLowerCase(Locale.ROOT)) {
        "normal" -> TypeNormal
        "fire" -> TypeFire
        "water" -> TypeWater
        "electric" -> TypeElectric
        "grass" -> TypeGrass
        "ice" -> TypeIce
        "fighting" -> TypeFighting
        "poison" -> TypePoison
        "ground" -> TypeGround
        "flying" -> TypeFlying
        "psychic" -> TypePsychic
        "bug" -> TypeBug
        "rock" -> TypeRock
        "ghost" -> TypeGhost
        "dragon" -> TypeDragon
        "dark" -> TypeDark
        "steel" -> TypeSteel
        "fairy" -> TypeFairy
        else -> Color.Black
    }
}

fun parseStatToAbbr(stat: Stat): String {
    return when(stat.stat.name.lowercase(Locale.ROOT)) {
        "hp" -> "HP"
        "attack" -> "Atk"
        "defense" -> "Def"
        "special-attack" -> "SpAtk"
        "special-defense" -> "SpDef"
        "speed" -> "Spd"
        else -> ""
    }
}