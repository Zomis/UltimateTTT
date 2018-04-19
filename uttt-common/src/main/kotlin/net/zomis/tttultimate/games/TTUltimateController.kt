package net.zomis.tttultimate.games

import net.zomis.tttultimate.TTBase
import net.zomis.tttultimate.TTPlayer
import net.zomis.tttultimate.TicUtils

class TTUltimateController(board: TTBase) : TTController(board) {
    // TODO: Try making it even more Ultimate by adding one more dimension, and use Map<TTBase, TTBase> activeBoards. Just for fun.
    private var activeBoard: TTBase? = null

    @Override
    override fun isAllowedPlay(tile: TTBase): Boolean {
        val area = tile.parent ?: return false
        val game = tile.parent.parent

        if (!tile.wonBy.equals(TTPlayer.NONE))
            return false
        if (area.wonBy.isExactlyOnePlayer)
            return false
        return if (game!!.isWon) false else activeBoard == null || activeBoard == area || activeBoard!!.wonBy !== TTPlayer.NONE

    }

    @Override
    public override fun performPlay(tile: TTBase): Boolean {
        var tile = tile
        tile.setPlayedBy(currentPlayer)
        activeBoard = TicUtils.getDestinationBoard(tile)
        nextPlayer()

        // Check for win condition on tile and if there is a win, cascade to it's parents
        do {
            tile.determineWinner()
            tile = if (tile.isWon) tile.parent else null
        } while (tile != null)

        return true
    }

    @Override
    override fun onReset() {
        this.activeBoard = null
    }

}
