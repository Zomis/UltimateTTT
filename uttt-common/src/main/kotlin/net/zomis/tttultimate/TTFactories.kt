package net.zomis.tttultimate

class TTFactories {

    fun factory(mnk: TTMNKParameters, next: TicFactory): TicFactory {
        return {parent, x, y -> TTBase(parent, x, y, mnk, next) }
    }

    fun classicMNK(width: Int, height: Int, consecutive: Int): TTBase {
        return TTBase(null, TTMNKParameters(width, height, consecutive), lastFactory)
    }

    fun classicMNK(mnk: Int): TTBase {
        return classicMNK(mnk, mnk, mnk)
    }

    fun ultimate(mnk: Int = 3): TTBase {
        return ultimateMNK(mnk, mnk, mnk)
    }

    fun ultimateMNK(width: Int, height: Int, consecutive: Int): TTBase {
        return TTBase(null, TTMNKParameters(width, height, consecutive), areaFactory)
    }

    fun othello(size: Int): TTBase {
        return TTBase(null, TTMNKParameters(size, size, size + 1), lastFactory)
    }

    private val mnkEmpty = TTMNKParameters(0, 0, 0)

    private val lastFactory: TicFactory = {parent, x, y ->
        TTBase(parent, x, y, mnkEmpty, null)
    }

    private val areaFactory: TicFactory = {parent, x, y ->
        TTBase(parent, x, y, parent.mnkParameters, lastFactory)
    }

}
