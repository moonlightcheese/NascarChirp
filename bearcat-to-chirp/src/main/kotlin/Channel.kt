package com.moonlightcheese

data class Channel(
    val ordinal: Int,
    val frequency: String,
    val name: String,
    val mode: String,   //NFM, FM, AM
    val squelchCode: Float,
    val squelchType: Char
)