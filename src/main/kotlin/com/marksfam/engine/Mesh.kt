package com.marksfam.engine

import java.lang.Math.TAU
import kotlin.math.cos
import kotlin.math.sin

data class Vertex(val x: Double, val y: Double, val z: Double)

fun rects(vararg i: Int): List<Int> {
    return IntProgression.fromClosedRange(0, i.size - 1, 4).flatMap {
        listOf(i[it], i[it + 1], i[it + 2], i[it + 1], i[it + 3], i[it + 2])
    }
}

fun cap(center: Int, vararg l: Int): List<Int> {
    return cap(center, l.asList())
}

fun cap(center: Int, l: List<Int>): List<Int> {
    return l.flatMapIndexed { index, i ->
        listOf(i, l[if (index + 1 < l.size) index + 1 else 0], center)
    }
}

fun dogEar(l: List<Int>): List<Int> {
    var i = 0
    var j = l.size - 1
    val ret = ArrayList<Int>()
    while (i < j) {
        ret.addAll(listOf(l[i], l[i + 1], l[j]))
        if (i + 1 < j - 1) {
            ret.addAll(listOf(l[i + 1], l[j - 1], l[j]))
        }
        i += 1
        j -= 1
    }

    return ret
}

fun extrude(base: List<Vertex>, offsetsAndScales: List<Pair<Float, Float>>): Mesh {
    val verts = ArrayList<Vertex>(base.size * (offsetsAndScales.size + 1))
    verts.addAll(base)
    for ((offset, scale) in offsetsAndScales) {
        for (baseVert in base) {
            verts.add(Vertex(baseVert.x * scale, baseVert.y * scale, offset.toDouble()))
        }
    }

    val indexes = ArrayList<Int>()

    // TODO: Build the side rects.
    // TODO: Run dogEar on the top and bottom to complete making the Mesh.
    // TODO: Rewrite Bolt and then write all the others using this.
    //       Although... no matter what I think of, snowflake will suck to do...
    // TODO: Test once it's done...
    return Mesh(verts, indexes)
}

data class Mesh(val vertexes: List<Vertex>, val indices: List<Int>) {
    companion object {
        val named = mutableMapOf(
                "bolt" to bolt,
                "star" to star5
        )
    }

    fun json(): Map<String, List<Number>> {
        return mapOf("indices" to indices, "positions" to vertexes.flatMap { listOf(it.x, it.y, it.z) })
    }
}

fun star(points: Int, innerRadius: Double, outerRadius: Double, centerThickness: Double): Mesh {
    val topAndBottom = listOf(Vertex(0.0, 0.0, centerThickness), Vertex(0.0, 0.0, -centerThickness))
    val increment = TAU / points

    val vertexes = topAndBottom + (0 ..< points).flatMap {
        listOf(Vertex(sin(it * increment) * outerRadius,
                      cos(it * increment) * outerRadius, 0.0),
                Vertex(sin((it + 0.5) * increment) * innerRadius,
                       cos((it + 0.5) * increment) * innerRadius, 0.0))
    }

    val topIndexes = (2 ..< ((points + 1) * 2)).toList()
    val indexes = cap(0, topIndexes) + cap(1, topIndexes.asReversed())
    return Mesh(vertexes, indexes)
}

val star5 = star(5, 0.25, 0.5, 0.25)
val bolt = Mesh(
        listOf(
                // Top cap:
                Vertex(-0.2,  0.5,  0.2),
                Vertex( 0.2,  0.5,  0.2),
                Vertex( 0.2,  0.5, -0.2),
                Vertex(-0.2,  0.5, -0.2),
                // Right side:
                Vertex( 0.0, -0.3,  0.2),
                Vertex( 0.0, -0.3, -0.2),
                Vertex( 0.2,  0.3,  0.2),
                Vertex( 0.2,  0.3, -0.2),
                // Bottom point:
                Vertex( 0.0, -0.5,  0.2),
                Vertex( 0.0, -0.5, -0.2),
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