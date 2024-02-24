package com.marksfam.engine

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.event
import java.util.*

val mapper = ObjectMapper()

interface PlayerListener {
    fun changed(player: Player)
}

class Player(val emitter: SseEmitter) {
    val id: UUID = UUID.randomUUID()
    val listeners = ArrayList<PlayerListener>()
    companion object {
        val instances = HashMap<UUID, Player>()
    }

    init {
        instances[id] = this
    }

    fun notifyListeners() {
        listeners.forEach { it.changed(this) }
    }

    var name: String = ""
        set(newName) {
            field = newName
            notifyListeners()
        }

    var score: Int = 0
        set(newScore) {
            field = newScore
            notifyListeners()
        }

    var lives: Int = 0
        set(newLives) {
            field = newLives
            notifyListeners()
        }

    private fun emitEvent(event: Set<ResponseBodyEmitter.DataWithMediaType>) {
        emitter.send(event)
    }

    private fun emitEvent(type: String, data: String) {
        emitEvent(event().name(type).data(data).build())
    }

    fun emitEvent(type: String, vararg data: Pair<String, Any>) {
        emitEvent(type, mapper.writeValueAsString(mapOf(*data)))
    }
}