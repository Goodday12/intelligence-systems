package org.example

import java.util.*
import kotlin.collections.HashMap
import kotlin.experimental.or

class TraversingAlgoDfs : TraversingAlgo {
    override fun runPacman(final_position: Pair<Int, Int>, board: Board) {
        with(board) {
            screenData[convertToRawIndex(final_position)] =
                    (screenData[convertToRawIndex(final_position)] or 32)
            dxDy.clear()
            dxDy[final_position] = Board.Direction.DEFAULT
            val distance =
                    HashMap<Pair<Int, Int>, Pair<Int, Int>>()
            val base = Pair(-1, -1)
            distance[final_position] = base
            val bfs = Stack<Pair<Int, Int>>()
            bfs.add(final_position) // adding starting point
            while (!bfs.isEmpty()) {
                val currentPosition = bfs.pop()
                val neighbours =
                        getNeightboursCells(currentPosition)
                for (neighbour in neighbours) {
                    if (isAvailableCell(neighbour.first)) {
                        if (!distance.containsKey(neighbour.first)) {
                            distance[neighbour.first] = currentPosition
                            bfs.add(neighbour.first)
                            dxDy[neighbour.first] = getOppositeDirection(neighbour.second)
                        }
                    }
                }
            }
        }
    }
}