package com.marksfam.engine

import kotlinx.datetime.Instant

class Countdown(position: ScreenPosition, prefix: String, endAt: Instant, action: () -> Unit) {
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