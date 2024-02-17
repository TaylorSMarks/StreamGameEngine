package com.marksfam.engine

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.until
import java.util.*
import kotlin.concurrent.schedule

class Countdown(position: ScreenPosition, var action: () -> Unit) {
    private val id = idGenerator.incrementAndGet()
    private val timer = Timer()
    private var task: TimerTask? = null

    init {
        println("Add countdown")
        controller.emitEvent("addCountdown",
            "x" to position.x,
            "y" to position.y,
            "id" to id)
    }

    private fun changed() {
        println("Countdown changed")
        controller.emitEvent("changeCountdown",
            "id" to id,
            "prefix" to prefix,
            "endAt" to endAt.toEpochMilliseconds())
    }

    var prefix = ""
        set(newPrefix) {
            field = newPrefix
            changed()
        }

    var endAt = Instant.DISTANT_FUTURE
        set(newEndAt) {
            field = newEndAt
            changed()
            task?.let { it.cancel() }
            task = timer.schedule(Clock.System.now().until(newEndAt, DateTimeUnit.MILLISECOND)) { action() }
        }
}