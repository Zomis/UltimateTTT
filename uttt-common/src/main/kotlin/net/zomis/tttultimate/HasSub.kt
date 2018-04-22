package net.zomis.tttultimate

/**
 * Interface for classes that can contain other objects, 'subs', in a rectangular way
 *
 * @param <T> Type of sub
</T> */
interface HasSub<out T> : Winnable {
    val winConds: Iterable<TTWinCondition>
    val sizeX: Int
    val sizeY: Int
    val consecutiveRequired: Int
    fun getSub(x: Int, y: Int): T?
    fun hasSubs(): Boolean
}
