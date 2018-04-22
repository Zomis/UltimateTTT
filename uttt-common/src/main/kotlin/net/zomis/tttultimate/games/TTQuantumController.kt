package net.zomis.tttultimate.games

import net.zomis.tttultimate.*

class TTQuantumController : TTController(TTFactories().ultimate()) {

    // http://en.wikipedia.org/wiki/Quantum_tic_tac_toe
    // TODO: Replay http://www.zomis.net/ttt/TTTWeb.html?mode=Quantum&history=22,23,25,26,27,37,57,67,66,65,64,62,61,52,51,60,51,43 -- no moves available

    private val subscripts = mutableMapOf<TTBase, Int>()
    private var firstPlaced: TTBase? = null
    private var collapse: Int? = null
    private var counter: Int = 0

    init {
        this.onReset()
    }

    override fun isAllowedPlay(tile: TTBase): Boolean {
        if (collapse == null && tile.isWon) {
            return false
        }

        if (collapse != null) {
            return subscripts[tile] === collapse
        }

        return if (firstPlaced != null) { // x Play two moves before switching, not on the same board
            tile.parent != firstPlaced
        } else !tile.isWon && !tile.parent!!.isWon
    }

    override fun performPlay(tile: TTBase): Boolean {
        if (collapse != null) {
            collapse = null
            // The new player should choose a field that should be collapsed
            performCollapse(tile)
            game.determineWinner()
            if (game.wonBy === TTPlayer.XO) {
                game.setPlayedBy(tieBreak())

            }
            return true
        }

        tile.setPlayedBy(currentPlayer)
        subscripts.put(tile, counter)

        if (firstPlaced != null) {
            firstPlaced = null
            nextPlayer()
            if (isEntaglementCycleCreated(tile)) {
                collapse = counter
                // when a cycle has been created the next player must choose the field that should be collapsed
            }
            counter++
        } else
            firstPlaced = tile.parent

        return true
    }

    private fun tieBreak(): TTPlayer {
        var lowestWin: TTWinCondition? = null
        for (cond in game.winConds) {
            val pl = cond.determineWinnerNew()
            if (pl === TTPlayer.NONE)
                continue
            if (lowestWin == null || highestSubscript(cond) < highestSubscript(lowestWin))
                lowestWin = cond
        }
        return lowestWin!!.determineWinnerNew()
    }

    private fun highestSubscript(cond: TTWinCondition): Int {
        var highest = 0
        for (tile in cond) {
            val value = subscripts[tile] ?: throw NullPointerException("Position doesn't have a subscript: $cond")
            highest = if (highest >= value) highest else value
        }
        return highest
    }


    private fun performCollapse(tile: TTBase) {
        if (!tile.isWon)
            throw IllegalArgumentException("Cannot collapse tile $tile")
        if (tile.hasSubs())
            throw AssertionError(subscripts.toString())
        if (tile.parent!!.isWon)
            throw AssertionError()

        val tangled = findEntanglement(tile)
        if (tangled != null) {
            subscripts.remove(tangled)
            tangled.reset()
        }

        val winner = tile.wonBy
        val value = subscripts.remove(tile)!!

        for (ff in TicUtils.getAllSubs(tile.parent)) {
            subscripts.remove(ff)
        }
        // TODO: Send ViewEvent to allow view to show what is happening step-by-step. Technically, this code should remove all but the tile itself.
        tile.parent.reset()
        tile.parent.setPlayedBy(winner)
        // then, when the winner has been declared, the smaller tile can be removed
        subscripts.put(tile.parent, value)
        collapseCheck()
    }

    private fun isEntaglementCycleCreated(tile: TTBase, scannedAreas: MutableSet<TTBase> = HashSet(), scannedTiles: MutableSet<TTBase> = HashSet()): Boolean {
        if (tile.parent == null || tile.hasSubs())
            throw IllegalArgumentException()

        scannedTiles.add(tile)
        if (scannedAreas.contains(tile.parent)) {
            return true
        }
        scannedAreas.add(tile.parent)

        val area = tile.parent

        val subs = TicUtils.getAllSubs(area)
        for (sub in subs) {
            if (sub == tile)
                continue
            if (!sub.isWon)
                continue

            if (scannedTiles.contains(sub))
                return true

            scannedTiles.add(sub)
            val tangled = findEntanglement(sub)
            if (tangled != null) {
                val recursive = isEntaglementCycleCreated(tangled, scannedAreas, scannedTiles)
                if (recursive)
                    return true
            }
        }
        return false
    }

    private fun collapseCheck() {
        // TEST: When a field does not have an entaglement anymore, collapse it

        for (ee in this.subscripts.entries) {
            // remove those that should be removed first, to make a clean scan later
            if (!ee.key.isWon) {
                subscripts.remove(ee.key)
            }
        }

        for (ee in this.subscripts.entries) {
            if (ee.key.hasSubs()) {
                continue
            }
            val match = findEntanglement(ee.key)
            if (match == null) {
                performCollapse(ee.key)
                collapseCheck()
                return
            }
        }
    }

    private fun findEntanglement(key: TTBase): TTBase? {
        if (!subscripts.containsKey(key))
            return null
        val match = subscripts[key]
        for (ee in this.subscripts.entries) {
            if (ee.key == key) {
                continue
            }
            if (ee.value == match) {
                return ee.key
            }
        }
        return null
    }

    override fun onReset() {
        this.subscripts.clear()
        this.collapse = null
        this.firstPlaced = null
        this.counter = 1
    }

    override fun getViewFor(tile: TTBase): String {
        var tileParent: TTBase? = tile
        if (!tileParent?.isWon!! && tileParent.parent!!.isWon) {
            tileParent = tileParent.parent
        }
        val sub = subscripts[tileParent]
        return super.getViewFor(tile) + (sub ?: "")
    }

}
