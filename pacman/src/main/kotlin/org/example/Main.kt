package org.example

import java.awt.EventQueue


fun main(args: Array<String>) {
        EventQueue.invokeLater {
                val ex = Pacman(
                        when(args.first().toInt()){
                                1,2,3,4 -> args.first().toInt()
                                else -> 1
                        }
                )
                ex.isVisible = true
        }
}

