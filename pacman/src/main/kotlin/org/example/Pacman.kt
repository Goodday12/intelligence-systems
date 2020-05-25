package org.example

import java.awt.EventQueue
import javax.swing.JFrame


class Pacman(private val flag: Int = 0) : JFrame() {

    init {
        initUI()
    }

    private fun initUI() {
        add(Board(flag))
        title = "Pacman"
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(380, 460)
    }
}