package net.zomis.tttultimate

enum class TTPlayer {

    NONE, X, O, XO;

    val isExactlyOnePlayer: Boolean
        get() = this == X || this == O

    operator fun next(): TTPlayer {
        if (!isExactlyOnePlayer)
            throw UnsupportedOperationException("Only possible to call .next() on a real player but it was called on " + this)
        return if (this == X) O else X
    }

    /**
     * Determine if this player is (also) another player.<br></br>
     * This is the same as `this.and(other) == other`
     *
     * @param other
     * @return
     */
    fun `is`(other: TTPlayer): Boolean {
        return this.and(other) == other
    }

    fun and(other: TTPlayer): TTPlayer {
        if (this == NONE || other == NONE) {
            return NONE
        }
        if (isExactlyOnePlayer && other.isExactlyOnePlayer) {
            return if (this == other) this else NONE
        }
        return if (this == XO) other else other.and(this)
    }

    fun or(other: TTPlayer): TTPlayer {
        if (this == NONE) {
            return other
        }
        if (other == NONE) {
            return this
        }
        if (this == XO) {
            return this
        }
        return if (this != other) XO else this
    }

    companion object {
        fun isExactlyOnePlayer(winner: TTPlayer?): Boolean {
            return winner != null && winner.isExactlyOnePlayer
        }
    }

}
