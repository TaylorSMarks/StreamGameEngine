package com.marksfam.engine

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.event
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

lateinit var controller: Controller

abstract class Controller {
    private val emitters = ArrayList<SseEmitter>()
    private val mapper = ObjectMapper()
    val defaultRoom = Room()

    init {
        controller = this
    }

    @GetMapping("/event-stream")
    fun getEventStream(): SseEmitter {
        val emitter = SseEmitter(Long.MAX_VALUE)
        emitters.add(emitter)
        emitter.onCompletion { emitters.remove(emitter) }
        emitter.onError { emitters.remove(emitter) }
        emitter.onTimeout { emitters.remove(emitter) }

        val player = Player(emitter)
        defaultRoom.addPlayer(player)
        all.forEach {
            if (it.visibleTo(player)) {
                it.showTo(player)
            }
        }
        onJoin(player)

        return emitter
    }

    @GetMapping("/clickModel/{playerId}/{modelId}")
    fun clickModel(@PathVariable playerId: UUID, @PathVariable modelId: Long) {
        println("GET clickModel/$playerId/$modelId")
        models[modelId]?.let { it.onClick?.invoke(playerId, it) }
    }

    @GetMapping("/mesh/{name}")
    fun getMesh(@PathVariable name: String): Map<String, List<Number>> {
        println("GET mesh/$name")
        return Mesh.named[name]!!.json()
    }

    abstract fun onJoin(player: Player)

    fun emitEvent(type: String, data: String) {
        // TODO: This still fails with a lot of ConcurrentModificationException... definitely need to change
        // emitters so that it doesn't fail all the damn time...
        val built = event().name(type).data(data).build()
        val failedEmitters = HashSet<SseEmitter>()
        emitters.forEach {
            try {
                it.send(built)
            } catch (e: IOException) {
                failedEmitters.add(it)
            }
        }
        emitters.removeAll(failedEmitters)
    }

    fun emitEvent(type: String, vararg data: Pair<String, Any>) {
        emitEvent(type, mapper.writeValueAsString(mapOf(*data)))
    }
}