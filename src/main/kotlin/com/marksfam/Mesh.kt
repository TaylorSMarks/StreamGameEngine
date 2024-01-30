package com.marksfam

data class Vertex(val x: Double, val y: Double, val z: Double)

fun rects(vararg i: Int): List<Int> {
    return IntProgression.fromClosedRange(0, i.size - 1, 4).flatMap {
        listOf(i[it], i[it + 1], i[it + 2], i[it + 1], i[it + 3], i[it + 2])
    }
}

data class Mesh(val vertexes: List<Vertex>, val indices: List<Int>) {
    companion object {
        val named = mutableMapOf(
                "bolt" to bolt
        )
    }

    fun json(): Map<String, List<Number>> {
        return mapOf("indices" to indices, "positions" to vertexes.flatMap { listOf(it.x, it.y, it.z) })
    }
}

val bolt = Mesh(
        listOf(
                // Top cap:
                Vertex(-0.2,  1.0,  0.2),
                Vertex( 0.2,  1.0,  0.2),
                Vertex( 0.2,  1.0, -0.2),
                Vertex(-0.2,  1.0, -0.2),
                // Right side:
                Vertex( 0.0, -0.3,  0.2),
                Vertex( 0.0, -0.3, -0.2),
                Vertex( 0.2,  0.3,  0.2),
                Vertex( 0.2,  0.3, -0.2),
                // Bottom point:
                Vertex( 0.0, -1.0,  0.2),
                Vertex( 0.0, -1.0, -0.2),
                // Left side:
                Vertex( 0.0,  0.3,  0.2),
                Vertex( 0.0,  0.3, -0.2),
                Vertex(-0.3, -0.3,  0.2),
                Vertex(-0.3, -0.3, -0.2)),
        rects(0, 1, 2, 3, // Top cap
                // Right side:
                1, 4, 5, 2,
                4, 6, 7, 5,
                6, 8, 9, 7,
                // Left side:
                8, 10, 11, 9,
                10, 12, 13, 11,
                12, 0, 3, 13,
                // Front rects:
                0, 1, 4, 12,
                12, 4, 6, 10,
                // Back rects:
                2, 3, 13, 5,
                5, 13, 7, 11
                ) + listOf(
                6, 10, 8, // Front triangle
                7, 11, 9) // Back triangle
        )