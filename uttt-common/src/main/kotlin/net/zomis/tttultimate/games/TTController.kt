package net.zomis.tttultimate.games

import net.zomis.tttultimate.TTBase
import net.zomis.tttultimate.TTMoveListener
import net.zomis.tttultimate.TTPlayer

abstract class TTController(val game: TTBase) {
    var currentPlayer = TTPlayer.X
        protected set
    private var moveListener: TTMoveListener? = null
    private var history: StringBuilder = StringBuilder()

    val CHARS = ('0'..'9') + ('a'..'z') + ('A'..'Z')

    val isGameOver: Boolean
        get() = game.isWon

    val wonBy: TTPlayer
        get() = game.wonBy

    abstract fun isAllowedPlay(tile: TTBase): Boolean

    fun play(tile: TTBase?): Boolean {
        if (tile == null)
            throw IllegalArgumentException("Tile to play at cannot be null.")

        if (!isAllowedPlay(tile)) {
            return false
        }
        if (!this.performPlay(tile))
            return false

        this.addToHistory(tile)

        if (this.moveListener != null)
            this.moveListener!!.onMove(tile)

        return true
    }

    private fun addToHistory(tile: TTBase) {
        if (!history.isEmpty()) {
            history.append(",")
        }
        history.append(CHARS[tile.globalX])
        history.append(CHARS[tile.globalY])
    }

    protected abstract fun performPlay(tile: TTBase): Boolean

    fun play(x: Int, y: Int): Boolean {
        return this.play(game.getSmallestTile(x, y))
    }

    protected fun nextPlayer() {
        currentPlayer = currentPlayer.next()
    }

    fun setOnMoveListener(moveListener: TTMoveListener) {
        this.moveListener = moveListener
    }

    fun makeMoves(history: String) {
        for (move in history.split(",")) {
            if (move.isEmpty())
                continue
            if (move.length != 2) {
                throw IllegalArgumentException("Unexcepted move length. $move")
            }

            val x = CHARS.indexOf(move[0])
            val y = CHARS.indexOf(move[1])

            val tile = game.getSmallestTile(x, y)
            if (!this.play(tile))
                throw IllegalStateException("Unable to make a move at $x, $y: $tile")
        }
    }

    fun saveHistory(): String {
        return this.history.toString()
    }

    fun reset() {
        this.currentPlayer = TTPlayer.X
        this.history = StringBuilder()
        this.game.reset()
        this.onReset()
    }

    protected abstract fun onReset()

    open fun getViewFor(tile: TTBase): String {
        return if (tile.isWon) tile.wonBy.toString() else ""
    }
}
