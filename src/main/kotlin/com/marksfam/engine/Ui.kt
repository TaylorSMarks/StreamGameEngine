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

class Countdown(position: ScreenPosition, prefix: String, endAt: Instant) {
    private val id = idGenerator.incrementAndGet()

    init {
        controller.emitEvent("addCountdown",
            "x" to position.x,
            "y" to position.y,
            "prefix" to prefix,
            "endAt" to endAt.toEpochMilliseconds(),
            "id" to id)
    }

    private fun changed() {
        controller.emitEvent("changeCountdown",
            "id" to id,
            "prefix" to prefix,
            "endAt" to endAt.toEpochMilliseconds())
    }

    var prefix = prefix
        set(newPrefix) {
            field = newPrefix
        }

    var endAt = endAt
        set(newEndAt) {
            field = newEndAt
        }
}