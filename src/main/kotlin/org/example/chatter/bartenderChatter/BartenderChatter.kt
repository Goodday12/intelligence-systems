package org.example.chatter.bartenderChatter

import org.example.chatter.Chatter
import org.example.chatter.ChatterState
import org.example.mapper.responsesToState
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import kotlin.system.exitProcess


class BartenderChatter(private val inputStream: InputStream, private val outputStream: OutputStream) : Chatter {
    override var state: ChatterState = ChatterState.GREET_STATE
    override var stateDictionary: List<String>? = null

    override fun chat() {
        val reader = BufferedReader(InputStreamReader(inputStream))
        outputStream.write("Start chat with greetings!\n".toByteArray())
        do {
            val line = reader.readLine()
            readAndSwitchState(line)
        } while (line !in setOf("exit"))
    }

    override fun readAndSwitchState(input: String) {
        stateDictionary = responsesToState[state]?.filterKeys {
            it.any { expectedInput ->
                expectedInput.toLowerCase().contains(input.toLowerCase())
            }
        }?.values?.random()
        val randomResponse = "${stateDictionary?.random()}\n"
        when (state) {
            ChatterState.GREET_STATE -> {
                createResponse(randomResponse, "What do you want to drink?\n", ChatterState.CHOOSE_STATE)
            }
            ChatterState.CHOOSE_STATE -> {
                createResponse(
                    randomResponse,
                    "What exactly do you want?\n",
                    ChatterState.values().find { it.stateName == input.toLowerCase() || input.contains(it.stateName) }
                        ?: ChatterState.CHOOSE_STATE
                )
            }
            ChatterState.WINE_STATE -> {
                createResponse(randomResponse, nextState = ChatterState.VODKA_STATE)
            }
            ChatterState.BEER_STATE -> {
                createResponse(randomResponse, nextState = ChatterState.VODKA_STATE)
            }
            ChatterState.WHISKEY_STATE -> {
                createResponse(randomResponse, nextState = ChatterState.VODKA_STATE)
            }
            ChatterState.VODKA_STATE -> {
                response("Enough for u! go home")
                exitProcess(0)
            }
        }
    }

    private fun createResponse(randomResponse: String?, additionalString: String = "", nextState: ChatterState) {
        when (randomResponse) {
            is String -> {
                response(
                    "$randomResponse $additionalString"
                )
                state = nextState
            }
            else -> {
                response("Didn't get ya, can you repeat")
            }
        }
    }

    override fun response(string: String) {
        outputStream.write(string.toByteArray())
    }
}