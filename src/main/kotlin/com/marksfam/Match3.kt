package com.marksfam

import com.marksfam.engine.*
import kotlinx.datetime.Clock
import org.springframework.web.bind.annotation.RestController
import java.lang.Thread.sleep
import java.util.*
import kotlin.collections.HashSet
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

val colorMeshPairs = listOf(
        Pair("#ff0000", "sphere"),  // TODO: Heart
        Pair("#ff8800", "bolt"),
        Pair("#ffff00", "star"),
        Pair("#00ff00", "sphere"),  // TODO: Rupee
        Pair("#4444ff", "sphere"),  // TODO: Raindrop
        Pair("#bb00bb", "sphere"),  // TODO: Crescent
        Pair("#ffffff", "sphere"))  // TODO: Snowflake

@RestController
class Match3: Controller() {
    private val turnLengthForPlayer = HashMap<Player, Double>()
    private val dropSpeed = 10.0f
    private val initialTurnLength = 20_000.0
    val grid = Grid<Model?>(7, 7, null)

    var priorClick: Model? = null

    val countdown = Countdown(ScreenPosition(0.5f, 1.0f)) {
        println("Timer ended")
        defaultRoom.currentPlayer?.let {
            it.lives -= 1
            println("Lives left: ${it.lives}")
            if (defaultRoom.lives > 0) {
                defaultRoom.lives -= 1
                println("Room lives left: ${defaultRoom.lives}")
            }
            startTurn()
        }
    }

    val nameTextInput = TextInput(ScreenPosition(0.5f, 0.5f))
    val startGameButton = Button(ScreenPosition(0.5f, 0.75f), "Start Game", false) { startGame() }

    lateinit var joinGameButton: Button

    init {
        joinGameButton = Button(ScreenPosition(0.5f, 0.625f), "Join Game") {
            Player.instances[it]?.let { player ->
                nameTextInput.get(player) {
                    player.name = it
                    nameTextInput.setVisibleTo(player, false)
                    joinGameButton.setVisibleTo(player, false)
                    startGameButton.setVisibleTo(player, true)
                }
            }
        }
    }

    fun onClick(playerId: UUID, it: Model) {
        if (playerId != defaultRoom.currentPlayer?.id) {
            println("Discard click from non-current player.")
            return
        }

        if (priorClick == null) {
            priorClick = it
            return
        }

        priorClick?.let { pc ->
            if (abs(pc.position.x - it.position.x) + abs(pc.position.y - it.position.y) == 1) {
                // They're adjacent. So swap them!
                countdown.pause()
                val pcPosition = pc.position
                pc.position = it.position
                it.position = pcPosition
                grid[it.position.x, it.position.y] = it
                grid[pc.position.x, pc.position.y] = pc
                priorClick = null
                sleep(500)
                if (detectAndRemoveMatches() == 0) {
                    // The swap wasn't valid, so undo it.
                    countdown.endIn -= 2.seconds
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
        grid.forEach(0..-3, 0..-1) { x, y, tile ->
            var xMatch = 1
            for (x2 in x + 1..<grid.width) {
                if (tile?.color == grid[x2, y]?.color) {
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

        grid.forEach(0..-1, 0..-3) { x, y, tile ->
            var yMatch = 1
            for (y2 in y + 1..<grid.height) {
                if (tile?.color == grid[x, y2]?.color) {
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

        toRemove.forEach {
            grid[it.position.x, it.position.y] = null
            it.destroy()
        }

        if (toRemove.isNotEmpty()) {
            defaultRoom.currentPlayer?.let {
                val award = (toRemove.size - 1) * 5  // 10 for 3, 15 for 4, 20 for 5, etc...
                it.score += award
                if (defaultRoom.lives > 0) {
                    defaultRoom.score += award
                }
            }
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
                // If matches were found, we'd recurse into dropTiles anyway,
                // so we only need to check if moves remain when matches were not found.
                if (!doAnyMovesRemain()) {
                    println("No moves remain, so resetting board.")
                    // Grant a small bonus of 5 points for clearing the board.
                    defaultRoom.currentPlayer?.let { it.score += 5 }
                    if (defaultRoom.lives > 0) { defaultRoom.score += 5 }
                    defaultRoom.currentPlayer = null
                    clear()
                    dropTiles()
                } else {
                    defaultRoom.currentPlayer?.let {
                        turnLengthForPlayer[it] = turnLengthForPlayer[it]!!.times(0.85)
                    }
                    startTurn()
                }
            }
        }
    }

    fun clear() {
        grid.forEach { x, y, tile ->
            tile?.destroy()
            grid[x, y] = null
        }
    }

    fun doAnyMovesRemain(): Boolean {
        return grid.any { x, y ->
                       matchCheckHelper(x, y, 2,  0, 3,  0) // 1. X XX
                    || matchCheckHelper(x, y, 1,  0, 3,  0) // 2. XX X
                    || matchCheckHelper(x, y, 1,  0, 2,  1) // 3.   X | 4.  X
                    || matchCheckHelper(x, y, 1,  1, 2,  0) //    XX  |    X X
                    || matchCheckHelper(x, y, 1,  1, 2,  1) // 5.  XX | 6. XX
                    || matchCheckHelper(x, y, 1,  0, 2, -1) //    X   |      X
                    || matchCheckHelper(x, y, 1, -1, 2,  0) // 7. X X | 8. X
                    || matchCheckHelper(x, y, 1, -1, 2, -1) //     X  |     XX
        }
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

    fun startGame() {
        clear()  // If there's already a board for some reason, make sure to dispose of it.
        defaultRoom.allPlayers().forEach {
            it.lives = 5
            it.score = 0
            turnLengthForPlayer[it] = initialTurnLength
            startGameButton.setVisibleTo(it, false)
            joinGameButton.setVisibleTo(it, false)
            nameTextInput.setVisibleTo(it, false)
        }
        defaultRoom.lives = 20
        defaultRoom.score = 0
        dropTiles()
        startTurn()
    }

    fun endGame() {
        countdown.pause()
        // TODO: Say who won.
        defaultRoom.allPlayers().forEach {
            joinGameButton.setVisibleTo(it, true)
            nameTextInput.setVisibleTo(it, true)
        }
    }

    fun startTurn() {
        defaultRoom.nextRandomPlayer()?.let {
            countdown.endAt = Clock.System.now() + turnLengthForPlayer[it]!!.roundToInt().milliseconds
            countdown.prefix = "${it.name}'s Turn"
        }

        if (defaultRoom.currentPlayer == null) {
            endGame()
        }
    }

    override fun onJoin(player: Player) {
        PlayerInfoCard(player, ScreenPosition(0f, 0.2f * defaultRoom.allPlayers().size))
        player.lives = 5
        turnLengthForPlayer[player] = initialTurnLength
    }
}