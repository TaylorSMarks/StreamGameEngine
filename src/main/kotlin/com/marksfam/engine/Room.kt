package com.marksfam.engine

class Room {
    val players = ArrayList<Player>()
    var currentPlayer: Player? = null
    var score = 0
    var lives = 0

    fun nextRandomPlayer(): Player? {
        val stillAlive = players.filter { it.lives > 0 }

        // The currentPlayer should only be half as likely to be the next player as any others still in the game -
        // But we definitely want it to be possible for them to have back-to-back (or back-to-back-to-back, or etc...)
        // turns.
        currentPlayer = (stillAlive + stillAlive.filter { currentPlayer != it }).randomOrNull()
        return currentPlayer
    }
}