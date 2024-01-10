package com.marksfam

import java.util.concurrent.atomic.AtomicLong

val idGenerator = AtomicLong()
val models = HashMap<Long, Model>()

data class Position(val x: Int, val y: Int, val z: Int = 0)

class Model(position: Position, val color: String, val onClick: ((Model) -> Unit)? = null) {
    private val id = idGenerator.incrementAndGet()

    init {
        models[id] = this
        controller.emitEvent("addModel",
            "x" to position.x,
            "y" to position.y,
            "z" to position.z,
            "color" to color,
            "id" to id,
            "clickable" to (onClick != null))
    }

    var position = position
        set(newPosition) {
            field = newPosition
            controller.emitEvent("moveModel",
                "id" to id,
                "x" to field.x,
                "y" to field.y,
                "z" to field.z,
                "duration" to 0.5)
        }

    fun destroy() {
        controller.emitEvent("removeModel", "id" to id)
    }
}