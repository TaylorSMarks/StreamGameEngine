package com.marksfam.engine

data class ScreenPosition(val x: Float, val y: Float)

class PlayerInfoCard(player: Player, position: ScreenPosition): Text(position, "#####\n###\n#"), PlayerListener {
    init {
        changed(player)
        player.listeners.add(this)
    }

    override fun changed(player: Player) {
        text = "${player.id}\n${player.score}\n${player.lives}"
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