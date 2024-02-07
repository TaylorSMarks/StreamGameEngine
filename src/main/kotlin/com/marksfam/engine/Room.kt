package com.marksfam.engine

import java.util.*
import kotlin.collections.HashMap

class Room {
    private val players = HashMap<UUID, Player>()
    var currentPlayer: Player? = null
    var score = 0
    var lives = 0

    fun allPlayers(): Collection<Player> {
        return players.values
    }

    fun addPlayer(player: Player) {
        players[player.id] = player
        player.emitEvent("welcome",
                "playerId" to player.id)
    }

    fun nextRandomPlayer(): Player? {
        val stillAlive = players.values.filter { it.lives > 0 }

        // The currentPlayer should only be half as likely to be the next player as any others still in the game -
        // But we definitely want it to be possible for them to have back-to-back (or back-to-back-to-back, or etc...)
        // turns.
        currentPlayer = (stillAlive + stillAlive.filter { currentPlayer != it }).randomOrNull()
        return currentPlayer
    }
}