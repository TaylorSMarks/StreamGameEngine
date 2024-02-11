package com.marksfam.engine

import kotlinx.datetime.Instant


data class ScreenPosition(val x: Float, val y: Float)

class Text(position: ScreenPosition, text: String) {
    private val id = idGenerator.incrementAndGet()

    init {
        controller.emitEvent("addText",
            "x" to position.x,
            "y" to position.y,
            "text" to text,
            "id" to id)
    }

    var text = text
        set(newText) {
            field = newText
            controller.emitEvent("changeText",
                "id" to id,
                "text" to text)
        }
}