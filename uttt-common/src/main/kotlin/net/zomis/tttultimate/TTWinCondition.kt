package net.zomis.tttultimate

class TTWinCondition : Iterable<Winnable> {

    private val winnables: List<Winnable>
    private val consecutive: Int

    constructor(vararg winnables: Winnable) : this(winnables.toList())

    constructor(winnables: List<Winnable>) : this(winnables, winnables.size)

    constructor(winnables: List<Winnable>, consecutive: Int) {
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

    fun hasWinnable(field: Winnable): Boolean {
        return winnables.contains(field)
    }

    fun size(): Int {
        return winnables.size
    }

    override fun iterator(): Iterator<Winnable> {
        return this.winnables.toMutableList().iterator()
    }

}
