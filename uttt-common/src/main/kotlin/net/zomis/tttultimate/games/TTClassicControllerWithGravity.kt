package net.zomis.tttultimate.games

import net.zomis.tttultimate.TTBase

class TTClassicControllerWithGravity(board: TTBase) : TTClassicController(board) {

    override fun isAllowedPlay(tile: TTBase): Boolean {
        val sup = super.isAllowedPlay(tile)
        if (!sup)
            return false

        val parent = tile.parent
        val below = parent!!.getSub(tile.x, tile.y + 1) ?: return true
        return below.isWon
    }

}
