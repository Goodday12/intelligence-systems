package org.example

import java.util.*
import kotlin.collections.HashMap
import kotlin.experimental.or

class TraversingAlgoBfs : TraversingAlgo {
    override fun runPacman(final_position: Pair<Int, Int>, board: Board) {
        with(board) {
            screenData[convertToRawIndex(final_position)] =
                    (screenData[convertToRawIndex(final_position)] or 32)
            dxDy.clear()
            dxDy[final_position] = Board.Direction.DEFAULT
            val distance =
                    HashMap<Pair<Int, Int>, Pair<Int, Pair<Int, Int>>>()
            val base = Pair(-1, -1)
            distance[final_position] = Pair(0, base)
            val bfs: Queue<Pair<Int, Int>> = LinkedList()
            bfs.add(final_position) // adding starting point
            while (!bfs.isEmpty()) {
                val currentPosition = bfs.remove()
                val minDistance = distance[currentPosition]!!.first
                val neighbours =
                        getNeightboursCells(currentPosition)
                for (neighbour in neighbours) {
                    if (isAvailableCell(neighbour.first)) {
                        if (distance.containsKey(neighbour.first)) {
                            val currentMinDistance = distance[neighbour.first]!!.first
                            if (currentMinDistance > minDistance + 1) {
                                distance[neighbour.first] =
                                        Pair(minDistance + 1, currentPosition)
                                bfs.add(neighbour.first)
                                dxDy[neighbour.first] = getOppositeDirection(neighbour.second)
                            }
                        } else {
                            distance[neighbour.first] = Pair(minDistance + 1, currentPosition)
                            bfs.add(neighbour.first)
                            dxDy[neighbour.first] = getOppositeDirection(neighbour.second)
                        }
                    }
                }
            }
        }
    }
}