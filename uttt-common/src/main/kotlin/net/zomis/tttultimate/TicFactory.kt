package net.zomis.tttultimate

interface TicFactory {
    fun construct(parent: TTBase, x: Int, y: Int): TTBase
}
