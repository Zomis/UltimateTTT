package net.zomis.tttultimate

enum class Direction8 constructor(val deltaX: Int, val deltaY: Int) {
    W(-1, 0), NW(-1, -1), N(0, -1), NE(1, -1), E(1, 0), SE(1, 1), S(0, 1), SW(-1, 1)
}
