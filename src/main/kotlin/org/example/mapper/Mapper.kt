package org.example.mapper

import org.example.chatter.ChatterState
import org.example.chatter.ChatterState.*


val responsesToState: Map<ChatterState, Map<List<String>, List<String>>> =
    mapOf(
        GREET_STATE to mapOf( listOf("hi", "hello", "greeting") to listOf("Hi", "Hello", "Greeting") ),
        CHOOSE_STATE to mapOf( listOf("beer") to listOf("We have a lot of taps"),
        listOf("wine") to listOf("Excellent choice"),
        listOf("whisky") to listOf("Mature"),
        listOf("vodka") to listOf("We don't have any, goodbye")),
        BEER_STATE to mapOf(listOf("ipa", "wheat", "lager", "ale") to listOf("We have that, here you are")),
        WINE_STATE to mapOf(listOf("red", "brut", "semisweet") to listOf("Here's your wine") ),
        WHISKEY_STATE to mapOf(listOf("single malt", "blended") to listOf("here's your whiskey"))
    )

