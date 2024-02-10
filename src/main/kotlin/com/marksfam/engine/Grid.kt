package com.marksfam.engine

class Grid<T>(val width: Int, val height: Int, val initial: T) {
    private val grid = ArrayList<T>(width * height)

    init {
        repeat(width * height) {
            grid.add(initial)
        }
    }

    operator fun get(x: Int, y: Int): T {
        checkBounds(x, y)
        return grid[y * width + x]
    }

    operator fun set(x: Int, y: Int, value: T) {
        checkBounds(x, y)
        grid[y * width + x] = value
    }

    fun setAll(value: T) {
        for (i in 0..<width * height) {
            grid[i] = value
        }
    }

    fun any(predicate: (Int, Int) -> Boolean): Boolean {
        return (0..<width).any { x -> (0..<height).any { y -> predicate(x, y) } }
    }

    fun forEach(action: (Int, Int, T) -> Unit) {
        forEach(0..-1, 0..-1, action)
    }

    fun forEach(xsIn: IntRange, ysIn: IntRange, action: (Int, Int, T) -> Unit) {
        val xs = negativeRangeHelper(xsIn, width)
        val ys = negativeRangeHelper(ysIn, height)
        xs.forEach { x -> ys.forEach { y -> action(x, y, get(x, y)) } }
    }

    private fun checkBounds(x: Int, y: Int) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            throw IndexOutOfBoundsException("$x, $y is out of bounds for Grid of size $width, $height")
        }
    }
}

private fun negativeIndexHelper(i: Int, max: Int): Int {
    return if (i >= 0) i else max + i
}

private fun negativeRangeHelper(r: IntRange, max: Int): IntRange {
    return IntRange(negativeIndexHelper(r.start, max), negativeIndexHelper(r.endInclusive, max))
}