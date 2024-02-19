package com.marksfam.engine

val all = ArrayList<Visible>()

interface Visible {
    fun visibleTo(player: Player): Boolean {
        return true
    }

    fun showTo(player: Player)
}