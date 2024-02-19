package com.marksfam.engine

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.until
import java.util.*
import kotlin.concurrent.schedule

class Countdown(val position: ScreenPosition, var action: () -> Unit): Visible {
    private val id = idGenerator.incrementAndGet()
    private val timer = Timer()
    private var task: TimerTask? = null

    var endAt = Instant.DISTANT_FUTURE
        set(newEndAt) {
            field = newEndAt
            changed()
            task?.let { it.cancel() }
            task = timer.schedule(Clock.System.now().until(newEndAt, DateTimeUnit.MILLISECOND)) { action() }
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
                "endAt" to endAt.toEpochMilliseconds() )
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
}