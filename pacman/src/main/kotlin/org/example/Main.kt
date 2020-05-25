package org.example

import java.awt.EventQueue


fun main(args: Array<String>) {
        EventQueue.invokeLater {
                val ex = Pacman(
                        when(args.first().toInt()){
                                0,1,2 -> args.first().toInt()
                                else -> 0
                        }
                )
                ex.isVisible = true
        }
}

