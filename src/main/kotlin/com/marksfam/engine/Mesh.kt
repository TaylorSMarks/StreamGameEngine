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

fun isConvex(a: Vertex, b: Vertex, c: Vertex): Boolean {
    return (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x) >= 0
}

fun inTriangle(a: Vertex, b: Vertex, c: Vertex, p: Vertex): Boolean {
    val eps = 0.0000001
    val bcy = b.y - c.y
    val pcx = p.x - c.x
    val cbx = c.x - b.x
    val pcy = p.y - c.y
    val acx = a.x - c.x
    val acy = a.y - c.y
    val l0 = (bcy * pcx + cbx * pcy) / ((bcy * acx + cbx * acy) + eps)
    if (l0 !in 0.0..1.0) {
        return false
    }

    val l1 = ((c.y - a.y) * pcx + acx * pcy) / ((bcy * acx + cbx * acy) + eps)
    if (l1 !in 0.0..1.0) {
        return false
    }

    val l2 = 1 - l0 - l1
    return (l2 in 0.0..1.0)
}

fun earClip(base: List<Vertex>): List<Int> {
    val l = base.mapIndexed { i, vertex -> Pair(vertex, i) }.toMutableList()
    val ret = ArrayList<Int>()

    repeat(l.size - 3) {
        // Get 3 sequential pairs from the list l.
        for (li in l.indices) {
            val a = l[li]
            val b = l[(li + 1) % l.size]
            val c = l[(li + 2) % l.size]

            if (!isConvex(a.first, b.first, c.first)) {
                continue
            }

            // Verify that no other point lies within the triangle abc.
            var maybeEar = true
            for (li2 in l.indices) {
                if (li2 != li && li2 != ((li + 1) % l.size) && li2 != ((li + 2) % l.size) && inTriangle(a.first, b.first, c.first, l[li2].first)) {
                    maybeEar = false
                    break
                }
            }

            if (!maybeEar) {
                continue
            }

            ret.addAll(listOf(a.second, b.second, c.second))
            l.removeAt((li + 1) % l.size)
            break
        }
    }

    if (l.size == 3) {
        ret.addAll(l.map { it.second })
    } else {
        throw Exception("Something went wrong while running earClip... l.size=${l.size}")
    }

    return ret
}

/** Note that if base is the bottom (facing away from the camera) and the offsets are positive,
 * Then the base should be anti-clockwise.
 *
 * If this doesn't make sense and it looks wrong, just try using .reverse() on the base.
 */
fun extrude(base: List<Vertex>, offsetsAndScales: List<Pair<Double, Double>>): Mesh {
    val verts = ArrayList<Vertex>(base.size * (offsetsAndScales.size + 1))
    val indexes = ArrayList<Int>()
    verts.addAll(base)
    var index = base.size
    for ((offset, scale) in offsetsAndScales) {
        for (baseVert in base) {
            verts.add(Vertex(baseVert.x * scale, baseVert.y * scale, offset))

            var nextIndex = index + 1
            if ((nextIndex % base.size) == 0) {
                nextIndex -= base.size
            }

            indexes.addAll(listOf(
                    index,
                    nextIndex,
                    index - base.size,
                    index - base.size,
                    nextIndex,
                    nextIndex - base.size
            ))

            index += 1
        }
    }

    val baseEars = earClip(base)
    val baseToTopOffset = base.size * offsetsAndScales.size
    indexes.addAll(baseEars)
    indexes.addAll(baseEars.reversed().map { it + baseToTopOffset })
    return Mesh(verts, indexes)
}

data class Mesh(val vertexes: List<Vertex>, val indices: List<Int>) {
    companion object {
        val named = mutableMapOf(
                "bolt" to bolt,
                "star" to star5,
                "drop" to drop,
                "rupee" to rupee,
                "heart" to heart
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
val bolt = extrude(listOf(
        Vertex(-0.25, 0.5, -0.2),
        Vertex(-0.5,  0.0, -0.2),
        Vertex( 0.1, -0.1, -0.2),
        Vertex( 0.0, -0.5, -0.2),
        Vertex( 0.5,  0.1, -0.2),
        Vertex( 0.0,  0.2, -0.2),
        Vertex( 0.25, 0.5, -0.2)),
        listOf(Pair(0.4, 1.0)))
val drop = extrude(listOf(
        Vertex(0.0, 0.4, -0.2),
        Vertex(-0.25, -0.15, -0.2),
        Vertex(-0.22, -0.28, -0.2),
        Vertex(-0.12, -0.37, -0.2),
        Vertex(0.0, -0.4, -0.2),
        Vertex(0.12, -0.37, -0.2),
        Vertex(0.22, -0.28, -0.2),
        Vertex(0.25, -0.15, -0.2)),
        listOf(Pair(0.2, 1.25), Pair(0.4, 1.0)))
val rupee = extrude(listOf(
        Vertex(0.0, 0.4, -0.2),
        Vertex(-0.2, 0.2, -0.2),
        Vertex(-0.2, -0.2, -0.2),
        Vertex(0.0, -0.4, -0.2),
        Vertex(0.2, -0.2, -0.2),
        Vertex(0.2, 0.2, -0.2)),
        listOf(Pair(0.2, 1.25), Pair(0.4, 1.0)))
val heart = extrude(listOf(
        Vertex(0.0, 0.30, -0.2),
        Vertex(-0.22, 0.40, -0.2),
        Vertex(-0.45, 0.25, -0.2),
        Vertex(0.0, -0.40, -0.2),
        Vertex(0.45, 0.25, -0.2),
        Vertex(0.22, 0.40, -0.2)),
        listOf(Pair(0.2, 1.1), Pair(0.4, 1.0)))