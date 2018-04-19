package net.zomis.tttultimate

class TTWinCondition : Iterable<TTBase> {

    private val winnables: List<TTBase>
    private val consecutive: Int

    constructor(vararg winnables: TTBase) : this(winnables.toList())

    constructor(winnables: List<TTBase>) : this(winnables, winnables.size)

    constructor(winnables: List<TTBase>, consecutive: Int) {
        if (winnables.isEmpty()) {
            throw IllegalArgumentException("Can't have an empty win condition!")
        }
        this.winnables = winnables.toMutableList()
        this.consecutive = consecutive
    }

    fun neededForWin(player: TTPlayer): Int {
        return winnables.size - hasCurrently(player)
    }

    fun isWinnable(byPlayer: TTPlayer): Boolean {
        return hasCurrently(byPlayer.next()) == 0
    }

    fun hasCurrently(player: TTPlayer): Int {
        var i = 0
        for (winnable in winnables) {
            if (winnable.wonBy.and(player) == player) {
                i++
            }
        }
        return i
    }

    fun determineWinnerNew(): TTPlayer {
        var winner: TTPlayer = TTPlayer.NONE

        val consecutivePlayers = IntArray(TTPlayer.values().size)
        for (winnable in winnables) {
            val current = winnable.wonBy
            for (pl in TTPlayer.values()) {
                val i = pl.ordinal
                if (pl.and(current) == pl) {
                    consecutivePlayers[i]++
                } else {
                    consecutivePlayers[i] = 0
                }

                if (consecutivePlayers[i] >= this.consecutive) {
                    winner = winner.or(pl)
                }
            }
        }
        return winner
    }

    fun hasWinnable(field: TTBase): Boolean {
        return winnables.contains(field)
    }

    fun size(): Int {
        return winnables.size
    }

    override fun iterator(): Iterator<TTBase> {
        return this.winnables.toMutableList().iterator()
    }

}
