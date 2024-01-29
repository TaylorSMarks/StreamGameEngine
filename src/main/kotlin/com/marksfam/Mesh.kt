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
                // TODO: Define the other 13 vertexes...
                Vertex(-0.2, 1.0, 0.2)),
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

// TODO: Planned meshes here...
// 1. Heart
// 2. Star
// 3. Crescent Moon
// 4. Lightning Bolt
// 5. Snowflake
// 6. Rupee
// 7. Raindrop

/*

Babylon's doc examples...

var customMesh = new BABYLON.Mesh("custom", scene);

var positions = [-5, 2, -3, -7, -2, -3, -3, -2, -3, 5, 2, 3, 7, -2, 3, 3, -2, 3];
var indices = [0, 1, 2, 3, 4, 5];

//Empty array to contain calculated values or normals added
var normals = [];

//Calculations of normals added
BABYLON.VertexData.ComputeNormals(positions, indices, normals);

var vertexData = new BABYLON.VertexData();

vertexData.positions = positions;
vertexData.indices = indices;
vertexData.normals = normals; //Assignment of normal to vertexData added

vertexData.applyToMesh(customMesh);


 */