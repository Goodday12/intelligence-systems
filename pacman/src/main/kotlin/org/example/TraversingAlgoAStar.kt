package org.example

import java.util.*
import kotlin.experimental.or


class TraversingAlgoAStar(private val startPosition: Pair<Int, Int>) : TraversingAlgo {

    override fun runPacman(finalPosition: Pair<Int, Int>, board: Board) {
        with(board){
            screenData[convertToRawIndex(finalPosition)] = (screenData[convertToRawIndex(finalPosition)] or 32)
            dxDy.clear()
            dxDy[finalPosition] = Board.Direction.DEFAULT
            val reachable: HashMap<Pair<Int, Int>, Board.Direction> = HashMap<Pair<Int, Int>, Board.Direction>()
            for (pos_dir in getNeightboursCells(finalPosition)) {
                if (!isAvailableCell(pos_dir.first)) continue
                reachable[pos_dir.first] = getOppositeDirection(pos_dir.second)
            }
            while (reachable.isNotEmpty()) {
                var nextPosition = Pair(0, 0)
                var minEuclideanDistance = 10000000
                for (position in reachable.keys) {
                    val euclideanDistance = calculateEuclideanDistance(position, startPosition)
                    if (euclideanDistance < minEuclideanDistance) {
                        minEuclideanDistance = euclideanDistance
                        nextPosition = position
                    }
                }
                val nextPosDir: Pair<Pair<Int, Int>, Board.Direction> = Pair(nextPosition, reachable[nextPosition]!!)
                dxDy[nextPosDir.first] = nextPosDir.second
                reachable.remove(nextPosition)
                for (pos_dir in getNeightboursCells(nextPosition)) {
                    val pos: Pair<Int, Int> = pos_dir.first
                    if (!isAvailableCell(pos)) continue
                    if (dxDy.containsKey(pos)) continue
                    if (reachable.containsKey(pos)) continue
                    reachable[pos_dir.first] = getOppositeDirection(pos_dir.second)
                }
            }
        }
    }
    private fun calculateEuclideanDistance(pos0: Pair<Int, Int>, pos1: Pair<Int, Int>): Int {
        return sqr(pos0.first - pos1.first) + sqr(pos0.second - pos1.second)
    }

    private fun sqr(x: Int): Int {
        return x * x
    }
}