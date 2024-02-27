package com.marksfam.engine

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.until
import java.util.*
import kotlin.concurrent.schedule
import kotlin.time.Duration
import kotlin.time.DurationUnit

class Countdown(val position: ScreenPosition, val stopAtEnd: Boolean, var action: () -> Unit): Visible {
    constructor(position: ScreenPosition, action: () -> Unit) : this(position, true, action)

    private val id = idGenerator.incrementAndGet()
    private val timer = Timer()
    private var task: TimerTask? = null

    var endAt = Instant.DISTANT_FUTURE
        set(newEndAt) {
            field = newEndAt
            changed()
            task?.let { it.cancel() }
            val msLeft = Clock.System.now().until(newEndAt, DateTimeUnit.MILLISECOND)
            if (msLeft <= 0) {
                action()
            } else {
                task = timer.schedule(msLeft) { action() }
            }
        }

    var endIn: Duration
        get() = endAt - Clock.System.now()
        set(newEndIn) {
            endAt = Clock.System.now() + newEndIn
        }

    init {
        println("Add countdown")
        controller.defaultRoom.allPlayers().forEach { showTo(it) }
        all.add(this)
    }

    override fun showTo(player: Player) {
        player.emitEvent("addCountdown",
                "x" to position.x,
                "y" to position.y,
                "id" to id,
                "stopAtEnd" to stopAtEnd,
                "endAt" to endAt.toEpochMilliseconds() )
    }

    private fun changed() {
        controller.emitEvent("changeCountdown",
            "id" to id,
            "prefix" to prefix,
            "endAt" to endAt.toEpochMilliseconds())
    }

    fun pause() {
        task?.let { it.cancel() }
        controller.emitEvent("pauseCountdown",
                "id" to id,
                "prefix" to prefix,
                "pauseAt" to "%.2f".format(endIn.toDouble(DurationUnit.SECONDS)))
    }

    var prefix = ""
        set(newPrefix) {
            field = newPrefix
            changed()
        }
}