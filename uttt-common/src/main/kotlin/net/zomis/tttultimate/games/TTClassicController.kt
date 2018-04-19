package net.zomis.tttultimate.games

import net.zomis.tttultimate.TTBase
import net.zomis.tttultimate.TTPlayer

open class TTClassicController(board: TTBase) : TTController(board) {

    @Override
    override fun isAllowedPlay(tile: TTBase): Boolean {
        return tile.parent != null && !tile.hasSubs() &&
                tile.parent.wonBy.equals(TTPlayer.NONE) &&
                tile.wonBy === TTPlayer.NONE
    }

    @Override
    public override fun performPlay(tile: TTBase): Boolean {
        tile.setPlayedBy(currentPlayer)
        tile.parent!!.determineWinner()
        nextPlayer()
        return true
    }

    @Override
    override fun onReset() {
    }

}
