package org.example

import java.awt.Color
import java.awt.Font
import java.awt.Image
import javax.swing.ImageIcon

data class UiSettings(
        val smallFont: Font = Font("Helvetica", Font.BOLD, 14),
        val ii: Image? = null,
        val dotColor: Color = Color(222, 149, 144),
        val destinyColor: Color = Color(192, 26, 41),
        var mazeColor: Color? = null,
        var inGame: Boolean = false,

        //    private boolean dying = false;
        val BLOCK_SIZE: Int = 24,
        val N_BLOCKS: Int = 15,
        val SCREEN_SIZE: Int = N_BLOCKS * BLOCK_SIZE,
        val PACMAN_SPEED: Int = 6,
        var pacman: Image? = null
){
    init {
        pacman = ImageIcon(this::class.java.classLoader.getResource("images/pacman.png")).image
    }
}