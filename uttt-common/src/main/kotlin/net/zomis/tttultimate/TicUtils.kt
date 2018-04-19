package net.zomis.tttultimate

import java.util.ArrayList

object TicUtils {
    /**
     * Get which board a tile will send the opponent to (in a TTTUltimate context)
     *
     * @param tile The tile to be played
     * @return The board which the tile directs to
     */
    fun getDestinationBoard(tile: TTBase): TTBase? {
        val parent = tile.getParent() ?: return null
        val grandpa = parent.getParent() ?: return null
        return grandpa!!.getSub(tile.getX(), tile.getY())
    }

    /**
     * Find the win conditions which contains a specific field
     *
     * @param field The field to look for
     * @param board Where to look for win conditions
     * @return A collection which only contains win conditions which contains the field
     */
    fun <E : Winnable> getWinCondsWith(field: E, board: HasSub<E>): Collection<TTWinCondition> {
        val coll = ArrayList()
        for (cond in board.winConds) {
            if (cond.hasWinnable(field))
                coll.add(cond)
        }
        return coll
    }

    /**
     * Get all smaller tiles/boards in a board
     *
     * @param board Board to scan
     * @return Collection of all smaller tiles/boards contained in board.
     */
    fun <T> getAllSubs(board: HasSub<T>): Collection<T> {
        val list = ArrayList()
        val sizeX = board.sizeX
        val sizeY = board.sizeY
        for (x in 0 until sizeX) {
            for (y in 0 until sizeY) {
                list.add(board.getSub(x, y))
            }
        }
        return list
    }

    /**
     * Recursively scan for smaller subs
     *
     * @param game The outermost object to scan
     * @return A collection containing all fields within the specified 'game' which do not have any subs
     */
    fun getAllSmallestFields(game: TTBase): Collection<TTBase> {
        val all = ArrayList()

        for (sub in TicUtils.getAllSubs<TTBase>(game)) {
            if (sub.hasSubs())
                all.addAll(getAllSmallestFields(sub))
            else
                all.add(sub)
        }
        return all
    }

    /**
     * Create win conditions
     *
     * @param board The board to create win conditions for
     * @return A list of all WinConditions that was created
     */
    fun setupWins(board: HasSub<out Winnable>): List<TTWinCondition> {
        if (!board.hasSubs()) {
            val list = ArrayList()
            list.add(TTWinCondition(board))
            return list
        }

        val consecutive = board.consecutiveRequired
        val conds = ArrayList()

        // Scan columns for a winner
        for (xx in 0 until board.sizeX) {
            newWin(conds, consecutive, loopAdd(board, xx, 0, 0, 1))
        }

        // Scan rows for a winner
        for (yy in 0 until board.sizeY) {
            newWin(conds, consecutive, loopAdd(board, 0, yy, 1, 0))
        }

        // Scan diagonals for a winner: Bottom-right
        for (yy in 0 until board.sizeY) {
            newWin(conds, consecutive, loopAdd(board, 0, yy, 1, 1))
        }
        for (xx in 1 until board.sizeX) {
            newWin(conds, consecutive, loopAdd(board, xx, 0, 1, 1))
        }

        // Scan diagonals for a winner: Bottom-left
        for (xx in 0 until board.sizeX) {
            newWin(conds, consecutive, loopAdd(board, xx, 0, -1, 1))
        }
        for (yy in 1 until board.sizeY) {
            newWin(conds, consecutive, loopAdd(board, board.sizeX - 1, yy, -1, 1))
        }

        return conds
    }

    private fun newWin(conds: List<TTWinCondition>, consecutive: Int, winnables: List<Winnable>) {
        if (winnables.size() >= consecutive)
        // shorter win conditions doesn't need to be added as they will never be able to win
            conds.add(TTWinCondition(winnables, consecutive))
    }

    private fun loopAdd(board: HasSub<out Winnable>,
                        xx: Int, yy: Int, dx: Int, dy: Int): List<Winnable> {
        var xx = xx
        var yy = yy
        val winnables = ArrayList()

        var tile: Winnable?
        do {
            tile = board.getSub(xx, yy)
            xx += dx
            yy += dy
            if (tile != null)
                winnables.add(tile)
        } while (tile != null)

        return winnables
    }

}
