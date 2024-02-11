package com.marksfam.engine

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.event
import java.util.*

val mapper = ObjectMapper()

class Player(val emitter: SseEmitter) {
    val id: UUID = UUID.randomUUID()

    var score: Int = 0
        set(newScore) {
            field = newScore
            println("Score: $newScore")
        }

    var lives: Int = 0

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