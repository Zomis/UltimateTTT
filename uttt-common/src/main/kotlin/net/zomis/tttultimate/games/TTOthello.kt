package net.zomis.tttultimate.games

import java.util.ArrayList

import net.zomis.common.Direction8
import net.zomis.tttultimate.TTBase
import net.zomis.tttultimate.TTFactories
import net.zomis.tttultimate.TTPlayer

class TTOthello @JvmOverloads constructor(size: Int = 8) : TTController(TTFactories().othello(size)) {
    init {
        this.onReset()
    }

    @Override
    override fun isAllowedPlay(tile: TTBase): Boolean {
        if (game.isWon)
            return false
        if (tile.hasSubs())
            return false
        return if (tile.isWon) false else !fieldCover(tile, currentPlayer).isEmpty()
    }

    private fun fieldCover(tile: TTBase, player: TTPlayer): List<TTBase> {
        if (!player.isExactlyOnePlayer)
            throw IllegalArgumentException()

        val tt = ArrayList()
        val parent = tile.parent
        for (dir in Direction8.values()) {
            var matchFound = false
            val thisDirection = ArrayList()
            var loop: TTBase? = tile
            do {
                loop = parent!!.getSub(loop!!.x + dir.getDeltaX(), loop.y + dir.getDeltaY())
                if (loop == null)
                    break
                if (loop.wonBy === TTPlayer.NONE)
                    break
                if (player.and(loop.wonBy) === player) {
                    matchFound = true
                }
                if (player !== loop.wonBy) {
                    thisDirection.add(loop)
                }
            } while (!matchFound)

            if (matchFound)
                tt.addAll(thisDirection)

        }
        return tt
    }


    @Override
    override fun performPlay(tile: TTBase): Boolean {
        val convertingTiles = fieldCover(tile, currentPlayer)
        for (ff in convertingTiles) {
            ff.setPlayedBy(currentPlayer)
        }
        tile.setPlayedBy(currentPlayer)
        nextPlayer()
        if (!isMovePossible(currentPlayer)) {
            nextPlayer()
            if (!isMovePossible(currentPlayer)) {
                val x = countSquares(TTPlayer.X)
                val o = countSquares(TTPlayer.O)
                var result = TTPlayer.NONE
                if (x >= o)
                    result = result.or(TTPlayer.X)
                if (o >= x)
                    result = result.or(TTPlayer.O)

                game.setPlayedBy(result)
            }
        }

        return true
    }

    private fun countSquares(player: TTPlayer): Int {
        var count = 0
        for (xx in 0 until game.sizeX) {
            for (yy in 0 until game.sizeY) {
                val sub = game.getSub(xx, yy)
                if (sub!!.wonBy.`is`(player))
                    count++
            }
        }

        return count
    }

    private fun isMovePossible(currentPlayer: TTPlayer): Boolean {
        for (xx in 0 until this.game.sizeX) {
            for (yy in 0 until this.game.sizeY) {
                if (this.isAllowedPlay(game.getSub(xx, yy)!!))
                    return true
            }
        }
        return false
    }

    @Override
    override fun onReset() {
        val board = this.game
        board.reset()
        val mX = board.sizeX / 2
        val mY = board.sizeY / 2
        board.getSub(mX - 1, mY - 1)!!.setPlayedBy(TTPlayer.X)
        board.getSub(mX - 1, mY)!!.setPlayedBy(TTPlayer.O)
        board.getSub(mX, mY - 1)!!.setPlayedBy(TTPlayer.O)
        board.getSub(mX, mY)!!.setPlayedBy(TTPlayer.X)
    }

}
