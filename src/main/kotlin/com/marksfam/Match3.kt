package com.marksfam

import org.springframework.web.bind.annotation.RestController
import java.lang.Thread.sleep
import kotlin.math.abs

val colorMeshPairs = listOf(
        Pair("#ff0000", "sphere"),  // TODO: Heart
        Pair("#ff8800", "bolt"),
        Pair("#ccff00", "sphere"),  // TODO: Star
        Pair("#00ff00", "sphere"),  // TODO: Rupee
        Pair("#4444ff", "sphere"),  // TODO: Raindrop
        Pair("#bb00bb", "sphere"),  // TODO: Crescent
        Pair("#ffffff", "sphere"))  // TODO: Snowflake

@RestController
class Match3: Controller() {
    private val dropSpeed = 10.0f
    val grid = Grid<Model?>(7, 7, null)

    var priorClick: Model? = null

    fun onClick(it: Model) {
        if (priorClick == null) {
            priorClick = it
            return
        }

        priorClick?.let { pc ->
            if (abs(pc.position.x - it.position.x) + abs(pc.position.y - it.position.y) == 1) {
                // They're adjacent. So swap them!
                val pcPosition = pc.position
                pc.position = it.position
                it.position = pcPosition
                grid[it.position.x, it.position.y] = it
                grid[pc.position.x, pc.position.y] = pc
                priorClick = null
                sleep(500)
                if (detectAndRemoveMatches() == 0) {
                    // The swap wasn't valid, so undo it.
                    it.position = pc.position
                    pc.position = pcPosition
                    grid[it.position.x, it.position.y] = it
                    grid[pc.position.x, pc.position.y] = pc
                }
            } else {
                // Not adjacent, so discard the earlier priorClick and make it this now instead.
                priorClick = it
            }
        }
    }

    fun detectAndRemoveMatches(): Int {
        val toRemove = HashSet<Model>()
        for (x in 0..grid.width - 3) {
            for (y in 0..<grid.height) {
                var xMatch = 1
                for (x2 in x + 1..<grid.width) {
                    if (grid[x, y]?.color == grid[x2, y]?.color) {
                        xMatch++
                        if (xMatch >= 3) {
                            toRemove.add(grid[x2, y]!!)
                            toRemove.add(grid[x2 - 1, y]!!)
                            toRemove.add(grid[x2 - 2, y]!!)
                        }
                    } else {
                        break
                    }
                }
            }
        }

        for (x in 0..<grid.width) {
            for (y in 0..grid.height - 3) {
                var yMatch = 1
                for (y2 in y + 1..<grid.height) {
                    if (grid[x, y]?.color == grid[x, y2]?.color) {
                        yMatch++
                        if (yMatch >= 3) {
                            toRemove.add(grid[x, y2]!!)
                            toRemove.add(grid[x, y2 - 1]!!)
                            toRemove.add(grid[x, y2 - 2]!!)
                        }
                    } else {
                        break
                    }
                }
            }
        }

        toRemove.forEach {
            grid[it.position.x, it.position.y] = null
            it.destroy()
        }

        if (toRemove.isNotEmpty()) {
            dropTiles()
        }
        return toRemove.size
    }

    fun dropTiles() {
        var tilesDropping = false
        for (x in 0..<grid.width) {
            var dropTo = -1
            for (y in 0..<grid.height) {
                if (grid[x, y] == null && dropTo == -1) {
                    dropTo = y
                    continue
                }

                if (dropTo != -1) {
                    grid[x, y]?.let {
                        it.move(Position(x, dropTo), dropSpeed)
                        grid[x, dropTo] = it
                        grid[x, y] = null
                        dropTo++
                    }
                }
            }

            if (dropTo != -1) {
                for (y in dropTo..<grid.height) {
                    val colorMeshPair = colorMeshPairs.random()
                    val newTile = Model(Position(x, y + 10), colorMeshPair.first, colorMeshPair.second, ::onClick)
                    grid[x, y] = newTile
                    newTile.move(Position(x, y), dropSpeed)
                    tilesDropping = true
                }
            }
        }

        if (tilesDropping) {
            sleep(800)
            if (detectAndRemoveMatches() == 0) {
                // If matches were found, we'd recurse into dropTiles anyways,
                // so we only need to check if moves remain if matches were not found.
                if (!doAnyMovesRemain()) {
                    println("No moves remain, so resetting board.")
                    clear()
                    dropTiles()
                }
            }
            detectAndRemoveMatches()
        }
    }

    fun clear() {
        for (x in 0..<grid.width) {
            for (y in 0..<grid.height) {
                grid[x, y]?.destroy()
                grid[x, y] = null
            }
        }
    }

    fun doAnyMovesRemain(): Boolean {
        for (x in 0..<grid.width) {
            for (y in 0..<grid.height) {
                if (   matchCheckHelper(x, y, 2,  0, 3,  0)    // X XX
                    || matchCheckHelper(x, y, 1,  0, 3,  0)    // XX X
                    || matchCheckHelper(x, y, 1,  0, 2,  1)    //   X   X
                    || matchCheckHelper(x, y, 1,  1, 2,  0)    // XX   X X
                    || matchCheckHelper(x, y, 1,  1, 2,  1)    //  XX  XX
                    || matchCheckHelper(x, y, 1,  0, 2, -1)    // X      X
                    || matchCheckHelper(x, y, 1, -1, 2,  0)    // X X  X
                    || matchCheckHelper(x, y, 1, -1, 2, -1)) { //  X    XX
                    println("Remaining move involving $x, $y")
                    return true
                }
            }
        }
        return false
    }

    fun matchCheckHelper(x: Int, y: Int, xOffset1: Int, yOffset1: Int, xOffset2: Int, yOffset2: Int): Boolean {
        val c = grid[x, y]?.color
        try {
            if (c == grid[x + xOffset1, y + yOffset1]?.color && c == grid[x + xOffset2, y + yOffset2]?.color) {
                return true
            }
        } catch (_: IndexOutOfBoundsException) { }

        return try {
            // Also check the mirror.
            c == grid[x + yOffset1, y + xOffset1]?.color && c == grid[x + yOffset2, y + xOffset2]?.color
        } catch (ex: IndexOutOfBoundsException) {
            false
        }
    }

    override fun onJoin() {
        dropTiles()
    }
}