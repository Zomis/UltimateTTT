package net.zomis.tttultimate.games

import net.zomis.tttultimate.TTBase
import net.zomis.tttultimate.TTPlayer
import net.zomis.tttultimate.TicUtils

class TTUltimateController(board: TTBase) : TTController(board) {
    // TODO: Try making it even more Ultimate by adding one more dimension, and use Map<TTBase, TTBase> activeBoards. Just for fun.
    var activeBoard: TTBase? = null
        private set

    override fun isAllowedPlay(tile: TTBase): Boolean {
        val area = tile.parent ?: return false
        val game = tile.parent.parent

        if (tile.wonBy != TTPlayer.NONE) {
            return false
        }
        if (area.wonBy.isExactlyOnePlayer) {
            return false
        }
        return if (game!!.isWon) {
            false
        } else {
            activeBoard == null || activeBoard == area || activeBoard!!.wonBy !== TTPlayer.NONE
        }
    }

    override fun performPlay(tile: TTBase): Boolean {
        tile.setPlayedBy(currentPlayer)
        activeBoard = TicUtils.getDestinationBoard(tile)
        nextPlayer()

        // Check for win condition on tile and if there is a win, cascade to it's parents
        var playAt: TTBase? = tile
        do {
            playAt?.determineWinner()
            playAt = if (playAt!!.isWon) playAt.parent else null
        } while (playAt != null)

        return true
    }

    override fun onReset() {
        this.activeBoard = null
    }

}
