package net.zomis.tttultimate.games

import net.zomis.tttultimate.TTBase
import net.zomis.tttultimate.TTPlayer

open class TTClassicController(board: TTBase) : TTController(board) {

    override fun isAllowedPlay(tile: TTBase): Boolean {
        return tile.parent != null && !tile.hasSubs() &&
                tile.parent.wonBy.equals(TTPlayer.NONE) &&
                tile.wonBy === TTPlayer.NONE
    }

    override fun performPlay(tile: TTBase): Boolean {
        tile.setPlayedBy(currentPlayer)
        tile.parent!!.determineWinner()
        nextPlayer()
        return true
    }

    override fun onReset() {
    }

}
