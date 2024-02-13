package com.marksfam.engine

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.until
import java.util.*
import kotlin.concurrent.schedule

class Countdown(position: ScreenPosition, prefix: String, endAt: Instant, var action: () -> Unit) {
    private val id = idGenerator.incrementAndGet()
    private val timer = Timer()
    private var task: TimerTask? = null

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
            task?.let { it.cancel() }
            task = timer.schedule(Clock.System.now().until(newEndAt, DateTimeUnit.MILLISECOND)) { action() }
        }
}