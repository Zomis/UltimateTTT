package net.zomis.tttultimate

class TTBase(val parent: TTBase?, val x: Int, val y: Int,
     val mnkParameters: TTMNKParameters, factory: TicFactory?) : Winnable, HasSub<TTBase> {

    private val subs: Array<Array<TTBase>>

    override val winConds: List<TTWinCondition>

    override var wonBy: TTPlayer = TTPlayer.NONE

    override val sizeX: Int
        get() = this.mnkParameters.width

    override val sizeY: Int
        get() = this.mnkParameters.height

    override val consecutiveRequired: Int
        get() = this.mnkParameters.consecutiveRequired

    val isWon: Boolean
        get() = wonBy !== TTPlayer.NONE

    val globalX: Int
        get() {
            if (parent == null) {
                return 0
            }
            return if (parent.parent == null) x else parent.x * parent.parent.sizeX + this.x
        }

    val globalY: Int
        get() {
            if (parent == null) {
                return 0
            }
            return if (parent.parent == null) y else parent.y * parent.parent.sizeY + this.y
        }

    constructor(parent: TTBase?, parameters: TTMNKParameters, factory: TicFactory) : this(parent, 0, 0, parameters, factory)

    init {
        this.subs = Array(mnkParameters.width) { xx ->
            Array(mnkParameters.height) { yy ->
                factory!!.invoke(this, xx, yy)
            }
        }
        this.winConds = TicUtils.setupWins(this)
    }

    fun determineWinner() {
        var winner: TTPlayer = TTPlayer.NONE
        for (cond in this.winConds) {
            winner = winner.or(cond.determineWinnerNew())
        }
        if (winner == TTPlayer.NONE && subs().all { it.isWon }) {
            winner = TTPlayer.BLOCKED
        }
        this.wonBy = winner
    }

    fun subs(): List<TTBase> {
        if (!hasSubs()) {
            return emptyList()
        }
        return subs.flatMap { it.toList() }
    }

    override fun getSub(x: Int, y: Int): TTBase? {
        if (!hasSubs() && x == 0 && y == 0) {
            return this
        }
        if (x < 0 || y < 0) {
            return null
        }
        return if (x >= sizeX || y >= sizeY) null else subs[x][y]
    }

    fun setPlayedBy(playedBy: TTPlayer) {
        this.wonBy = playedBy
    }

    override fun hasSubs(): Boolean {
        return sizeX != 0 && sizeY != 0
    }

    override fun toString(): String {
        return "{Pos $x, $y; Size $sizeX, $sizeY; Played by $wonBy. Parent is $parent}"
    }

    fun reset() {
        this.setPlayedBy(TTPlayer.NONE)
        subs().forEach { it.reset() }
    }

    fun getSmallestTile(x: Int, y: Int): TTBase? {
        val topLeft = getSub(0, 0) ?: return null
        val grandParent = topLeft.hasSubs()

        if (!grandParent) {
            return this.getSub(x, y)
        }

        val subX = x / topLeft.sizeX
        val subY = y / topLeft.sizeY
        val board = getSub(subX, subY) ?: throw NullPointerException("No such smallest tile found: $x, $y")
        return board.getSub(x - subX * sizeX, y - subY * sizeY)
    }

}
