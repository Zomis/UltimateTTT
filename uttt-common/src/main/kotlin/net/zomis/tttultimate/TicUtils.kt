package net.zomis.tttultimate

class TicUtils {
    /**
     * Get which board a tile will send the opponent to (in a TTTUltimate context)
     *
     * @param tile The tile to be played
     * @return The board which the tile directs to
     */
    fun getDestinationBoard(tile: TTBase): TTBase? {
        val parent = tile.parent ?: return null
        val grandpa = parent.parent ?: return null
        return grandpa.getSub(tile.x, tile.y)
    }

    /**
     * Find the win conditions which contains a specific field
     *
     * @param field The field to look for
     * @param board Where to look for win conditions
     * @return A collection which only contains win conditions which contains the field
     */
    fun getWinCondsWith(field: TTBase, board: TTBase): Collection<TTWinCondition> {
        val coll = mutableListOf<TTWinCondition>()
        for (cond in board.winConds) {
            if (cond.hasWinnable(field)) {
                coll.add(cond)
            }
        }
        return coll
    }

    /**
     * Get all smaller tiles/boards in a board
     *
     * @param board Board to scan
     * @return Collection of all smaller tiles/boards contained in board.
     */
    fun getAllSubs(board: TTBase): Collection<TTBase> {
        val list = mutableListOf<net.zomis.tttultimate.TTBase>()
        val sizeX = board.sizeX
        val sizeY = board.sizeY
        for (x in 0 until sizeX) {
            for (y in 0 until sizeY) {
                list.add(board.getSub(x, y)!!)
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
        val all = mutableListOf<TTBase>()

        for (sub in getAllSubs(game)) {
            if (sub.hasSubs()) {
                all.addAll(getAllSmallestFields(sub))
            } else {
                all.add(sub)
            }
        }
        return all
    }

    /**
     * Create win conditions
     *
     * @param board The board to create win conditions for
     * @return A list of all WinConditions that was created
     */
    fun setupWins(board: TTBase): List<TTWinCondition> {
        if (!board.hasSubs()) {
            val list = mutableListOf<TTWinCondition>()
            list.add(TTWinCondition(board))
            return list
        }

        val consecutive = board.consecutiveRequired
        val conds = mutableListOf<TTWinCondition>()

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

    private fun newWin(conds: MutableList<TTWinCondition>, consecutive: Int, winnables: List<TTBase>) {
        // shorter win conditions doesn't need to be added as they will never be able to win
        if (winnables.size >= consecutive) {
            conds.add(TTWinCondition(winnables, consecutive))
        }
    }

    private fun loopAdd(board: TTBase,
                        xxStart: Int, yyStart: Int, dx: Int, dy: Int): List<TTBase> {
        var xx = xxStart
        var yy = yyStart
        val winnables = mutableListOf<TTBase>()

        var tile: TTBase?
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
