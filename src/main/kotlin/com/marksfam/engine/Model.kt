package com.marksfam.engine

import java.util.*
import java.util.concurrent.atomic.AtomicLong
import kotlin.collections.HashMap
import kotlin.math.pow
import kotlin.math.sqrt

val idGenerator = AtomicLong()
val models = HashMap<Long, Model>()

data class Position(val x: Int, val y: Int, val z: Int = 0) {
    fun distanceTo(o: Position) =
            sqrt((x - o.x).toFloat().pow(2) + (y - o.y).toFloat().pow(2) + (z - o.z).toFloat().pow(2))
}

class Model(position: Position, val color: String, val mesh: String, val onClick: ((UUID, Model) -> Unit)? = null): Visible {
    private val id = idGenerator.incrementAndGet()
    private var emittedViaMoveAlready = false

    var position = position
        set(newPosition) {
            field = newPosition
            if (emittedViaMoveAlready) {
                emittedViaMoveAlready = false
            } else {
                controller.emitEvent("moveModel",
                        "id" to id,
                        "x" to field.x,
                        "y" to field.y,
                        "z" to field.z,
                        "duration" to 0.5)
            }
        }

    init {
        models[id] = this
        controller.defaultRoom.allPlayers().forEach { showTo(it) }
        all.add(this)
    }

    override fun showTo(player: Player) {
        player.emitEvent("addModel",
                "x" to position.x,
                "y" to position.y,
                "z" to position.z,
                "color" to color,
                "id" to id,
                "mesh" to mesh,
                "clickable" to (onClick != null))
    }

    fun move(newPosition: Position, speed: Float) {
        val duration = position.distanceTo(newPosition) / speed
        controller.emitEvent("moveModel",
                "id" to id,
                "x" to newPosition.x,
                "y" to newPosition.y,
                "z" to newPosition.z,
                "duration" to duration)
        emittedViaMoveAlready = true
        position = newPosition
    }

    fun destroy() {
        controller.emitEvent("removeModel", "id" to id)
        models.remove(id)
        all.remove(this)
    }
}