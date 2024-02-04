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

    private fun checkBounds(x: Int, y: Int) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            throw IndexOutOfBoundsException("$x, $y is out of bounds for Grid of size $width, $height")
        }
    }
}