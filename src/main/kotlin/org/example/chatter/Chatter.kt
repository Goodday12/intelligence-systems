package org.example.chatter

import java.io.InputStream
import java.io.OutputStream

interface Chatter {
    val state: ChatterState
    var stateDictionary: List<String>?

    fun chat()
    fun readAndSwitchState(input: String)
    fun response(string: String)
}
