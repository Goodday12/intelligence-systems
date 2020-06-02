package org.example

import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.util.*
import javax.swing.JPanel
import javax.swing.Timer
import kotlin.collections.HashMap
import kotlin.experimental.and


class Board(private val flag: Int = 0, private val uiSettings: UiSettings = UiSettings()) : JPanel(), ActionListener {

    private var d: Dimension? = null
    private lateinit var algo: TraversingAlgo

    private var score = 0
    private lateinit var dx: IntArray
    private lateinit var dy: IntArray

    private var pacmanX = 0
    private var pacmanY = 0
    private var pacmandX = 0
    private var pacmandY = 0
    private val reqDx = 0
    private val reqDy = 0
    private var viewDx = 0
    private var viewDy = 0

    enum class Direction(val dx: Int, val dy: Int) {
        LEFT(-1, 0), RIGHT(1, 0), UP(0, -1), DOWN(0, 1), DEFAULT(0, 0);

    }

    private val allDirections = listOf(
        Direction.LEFT,
        Direction.RIGHT,
        Direction.UP,
        Direction.DOWN
    )
    val dxDy = HashMap<Pair<Int, Int>, Direction>()
    private val levelData = shortArrayOf(
            19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            9, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 28
    )

    lateinit var screenData: ShortArray
    private var timer: Timer? = null
    private fun initBoard() {
        isFocusable = true
        background = Color.black
    }

    private fun initVariables() {
        screenData = ShortArray(uiSettings.N_BLOCKS * uiSettings.N_BLOCKS)
        uiSettings.mazeColor = Color(0, 0, 200)
        d = Dimension(400, 400)
        dx = IntArray(4)
        dy = IntArray(4)
        timer = Timer(40, this)
        timer!!.start()
    }

    override fun addNotify() {
        super.addNotify()
        initGame()
    }


    private fun playGame(g2d: Graphics2D) {
        movePacman()
        drawPacman(g2d)
    }

    private fun drawScore(g: Graphics2D) {
        g.font = uiSettings.smallFont
        g.color = Color(96, 128, 255)
        val s = "Score: $score"
        g.drawString(s, uiSettings.SCREEN_SIZE / 2 + 96, uiSettings.SCREEN_SIZE + 16)
    }

    private fun movePacman() {
        val pos: Int
        val ch: Short
        if (reqDx == -pacmandX && reqDy == -pacmandY) {
            pacmandX = reqDx
            pacmandY = reqDy
            viewDx = pacmandX
            viewDy = pacmandY
        }
        if (pacmanX % uiSettings.BLOCK_SIZE == 0 && pacmanY % uiSettings.BLOCK_SIZE == 0) {
            pos = pacmanX / uiSettings.BLOCK_SIZE + uiSettings.N_BLOCKS * (pacmanY / uiSettings.BLOCK_SIZE)
            ch = screenData[pos]
            if (ch and 16 != 0.toShort()) {
                screenData[pos] = (ch and 15) as Short
                score++
                if (ch and 32 != 0.toShort()) {
                    initGame()
                }
            }
//            if (reqDx != 0 || reqDy != 0) {
//                if (!isStandstill(ch)) {
//                    pacmandX = reqDx
//                    pacmandY = reqDy
//                    pacmandY = reqDy
//                    viewDx = pacmandX
//                    viewDy = pacmandY
//                }
//            }
            val currentPosition =
                Pair(pacmanX / uiSettings.BLOCK_SIZE, pacmanY / uiSettings.BLOCK_SIZE)
            pacmandX = dxDy[currentPosition]!!.dx
            pacmandY = dxDy[currentPosition]!!.dy
            viewDx = pacmandX
            viewDy = pacmandY

            if (isStandstill(ch)) {
                pacmandX = 0
                pacmandY = 0
            }
        }
        pacmanX += uiSettings.PACMAN_SPEED * pacmandX
        pacmanY += uiSettings.PACMAN_SPEED * pacmandY
    }

    private fun isStandstill(ch: Short): Boolean {
        return (pacmandX == -1 && pacmandY == 0 && ch and 1 != 0.toShort() // ch & 1 - left wall
                || pacmandX == 1 && pacmandY == 0 && ch and 4 != 0.toShort() // ch & 4 - right wall
                || pacmandX == 0 && pacmandY == -1 && ch and 2 != 0.toShort() // ch & 2 - up wall
                || pacmandX == 0 && pacmandY == 1 && ch and 8 != 0.toShort()) // ch & 8 - down wall
    }

    private fun drawPacman(g2d: Graphics2D) {
        g2d.drawImage(uiSettings.pacman, pacmanX + 1, pacmanY + 1, this)
    }


    private fun drawMaze(g2d: Graphics2D) {
        var i: Short = 0
        var x: Int
        var y = 0
        while (y < uiSettings.SCREEN_SIZE) {
            x = 0
            while (x < uiSettings.SCREEN_SIZE) {
                g2d.color = uiSettings.mazeColor
                g2d.stroke = BasicStroke(2F)
                if (screenData[i.toInt()] and 1 != 0.toShort()) {
                    g2d.drawLine(x, y, x, y + uiSettings.BLOCK_SIZE - 1)
                }
                if (screenData[i.toInt()] and 2 != 0.toShort()) {
                    g2d.drawLine(x, y, x + uiSettings.BLOCK_SIZE - 1, y)
                }
                if (screenData[i.toInt()] and 4 != 0.toShort()) {
                    g2d.drawLine(
                        x + uiSettings.BLOCK_SIZE - 1, y, x + uiSettings.BLOCK_SIZE - 1,
                        y + uiSettings.BLOCK_SIZE - 1
                    )
                }
                if (screenData[i.toInt()] and 8 != 0.toShort()) {
                    g2d.drawLine(
                        x, y + uiSettings.BLOCK_SIZE - 1, x + uiSettings.BLOCK_SIZE - 1,
                        y + uiSettings.BLOCK_SIZE - 1
                    )
                }
                if (screenData[i.toInt()] and 16 != 0.toShort()) {
                    g2d.color = uiSettings.dotColor
                    g2d.fillRect(x + 11, y + 11, 2, 2)
                }
                if (screenData[i.toInt()] and 32 != 0.toShort()) {
                    g2d.color = uiSettings.destinyColor
                    g2d.fillRect(x + 11, y + 11, 10, 10)
                }
                i++
                x += uiSettings.BLOCK_SIZE
            }
            y += uiSettings.BLOCK_SIZE
        }
    }

    private fun initGame() {
        score = 0
        initLevel()
    }

    fun convertToRawIndex(position: Pair<Int, Int>): Int {
        val x = position.first
        val y = position.second
        return x + uiSettings.N_BLOCKS * y
    }

    fun isAvailableCell(position: Pair<Int, Int>): Boolean {
        val ch = screenData[convertToRawIndex(position)]
        return ch and 16 != 0.toShort()
    }

    private fun isValidCell(position: Pair<Int, Int>): Boolean {
        return position.first in 0 until uiSettings.N_BLOCKS && position.second >= 0 && position.second < uiSettings.N_BLOCKS
    }

    fun getNeightboursCells(current_position: Pair<Int, Int>): List<Pair<Pair<Int, Int>, Direction>> {
        val result: MutableList<Pair<Pair<Int, Int>, Direction>> =
            ArrayList()
        for (direction in allDirections) {
            val position = current_position.first + direction.dx to current_position.second + direction.dy
            if (isValidCell(position)) {
                result.add(Pair(position, direction))
            }
        }
        return result
    }

    fun getOppositeDirection(direction: Direction): Direction
        = when (direction) {
            Direction.LEFT -> Direction.RIGHT
            Direction.RIGHT -> Direction.LEFT
            Direction.UP -> Direction.DOWN
            Direction.DOWN -> Direction.UP
            else -> Direction.DEFAULT
        }

    private val randomDestinyPosition: Pair<Int, Int>
        get() {
            var x = (Math.random() * uiSettings.N_BLOCKS).toInt()
            var y = (Math.random() * uiSettings.N_BLOCKS).toInt()
            while (screenData[convertToRawIndex(Pair(x, y))] and 16 == 0.toShort() || x == 0 && y == 0) {
                x = (Math.random() * uiSettings.N_BLOCKS).toInt()
                y = (Math.random() * uiSettings.N_BLOCKS).toInt()
            }
            return Pair(x, y)
        }

    private fun initLevel() {
        var i : Int = 0
        while (i < uiSettings.N_BLOCKS * uiSettings.N_BLOCKS) {
            screenData[i] = levelData[i].toShort()
            i++
        }
        algo = when (flag) {
            1 -> TraversingAlgoBfs()
            2 -> TraversingAlgoDfs()
            3 -> TraversingAlgoAStar(Pair(pacmanX / uiSettings.BLOCK_SIZE, pacmanY / uiSettings.BLOCK_SIZE))
            4 -> TraversingAlgoGready()
            else -> throw IllegalArgumentException()
        }
        algo.runPacman(randomDestinyPosition, this)

    }

    public override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        doDrawing(g)
    }

    private fun doDrawing(g: Graphics) {
        val g2d = g as Graphics2D
        g2d.color = Color.black
        g2d.fillRect(0, 0, d!!.width, d!!.height)
        drawMaze(g2d)
        drawScore(g2d)
        playGame(g2d)
        g2d.drawImage(uiSettings.ii, 5, 5, this)
        Toolkit.getDefaultToolkit().sync()
        g2d.dispose()
    }

    override fun actionPerformed(e: ActionEvent) {
        repaint()
    }

    init {
        initVariables()
        initBoard()
    }
}