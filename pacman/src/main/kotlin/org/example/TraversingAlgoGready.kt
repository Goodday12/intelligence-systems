package org.example

import java.util.*
import kotlin.experimental.or


class TraversingAlgoGready : TraversingAlgo {
    override fun runPacman(finalPosition: Pair<Int, Int>, board: Board) {
        with(board){
            screenData[convertToRawIndex(finalPosition)] = (screenData[convertToRawIndex(finalPosition)] or 32) as Short

            dxDy.clear()
            dxDy[finalPosition] = Board.Direction.DEFAULT

            val reachable: HashMap<Pair<Int, Int>, Board.Direction> = HashMap<Pair<Int, Int>, Board.Direction>()
            for (pos_dir in getNeightboursCells(finalPosition)) {
                if (!isAvailableCell(pos_dir.first)) continue
                reachable[pos_dir.first] = getOppositeDirection(pos_dir.second)
            }

            while (reachable.isNotEmpty()) {
                var nextPosition = Pair(0, 0)
                val index: Int = (Math.random() * (reachable.size)).toInt()
                var i = 0
                for (position in reachable.keys) {
                    nextPosition = position
                    if (i == index) {
                        break
                    }
                    i++
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


}