package com.marksfam.engine

import java.util.*
import kotlin.collections.HashMap

data class ScreenPosition(val x: Float, val y: Float)

class PlayerInfoCard(player: Player, position: ScreenPosition): Text(position, "#####\n###\n#"), PlayerListener {
    init {
        changed(player)
        player.listeners.add(this)
    }

    override fun changed(player: Player) {
        text = "${player.name}\n${player.score}\n${player.lives}"
    }
}

open class Text(val position: ScreenPosition, text: String): Visible {
    private val id = idGenerator.incrementAndGet()

    var text = text
        set(newText) {
            field = newText
            controller.emitEvent("changeText",
                    "id" to id,
                    "text" to text)
        }

    init {
        controller.defaultRoom.allPlayers().forEach { showTo(it) }
        all.add(this)
    }

    override fun showTo(player: Player) {
        player.emitEvent("addText",
                "x" to position.x,
                "y" to position.y,
                "text" to text,
                "id" to id)
    }
}

class Button(val position: ScreenPosition, val text: String, val onClick: ((UUID) -> Unit)): Visible {
    private val id = idGenerator.incrementAndGet()

    companion object {
        val instances = HashMap<Long, Button>()
    }

    init {
        controller.defaultRoom.allPlayers().forEach { showTo(it) }
        all.add(this)
        instances[id] = this
    }

    override fun showTo(player: Player) {
        player.emitEvent("addButton",
                "x" to position.x,
                "y" to position.y,
                "id" to id)
    }
}

class TextInput(val position: ScreenPosition): Visible {
    private val id = idGenerator.incrementAndGet()
    val handleMap = HashMap<UUID, (String) -> Unit>()

    companion object {
        val instances = HashMap<Long, TextInput>()
    }

    init {
        controller.defaultRoom.allPlayers().forEach { showTo(it) }
        all.add(this)
        instances[id] = this
    }

    override fun showTo(player: Player) {
        player.emitEvent("addTextInput",
                "x" to position.x,
                "y" to position.y,
                "id" to id)
    }

    fun get(player: Player, handle: (String) -> Unit) {
        handleMap[player.id] = handle
        player.emitEvent("getTextInput",
                "id" to id)
    }
}