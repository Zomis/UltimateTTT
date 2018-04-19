package net.zomis.tttultimate

class TTFactories {

    class Factory(private val mnk: TTMNKParameters, private val next: TicFactory) : TicFactory {
        override fun construct(parent: TTBase, x: Int, y: Int): TTBase {
            return TTBase(parent, x, y, mnk, next)
        }
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

    companion object {
        private val mnkEmpty = TTMNKParameters(0, 0, 0)

        private val lastFactory = object : TicFactory {
            override fun construct(parent: TTBase, x: Int, y: Int): TTBase {
                return TTBase(parent, x, y, mnkEmpty, null)
            }
        }
        private val areaFactory = object : TicFactory {
            override fun construct(parent: TTBase, x: Int, y: Int): TTBase {
                return TTBase(parent, x, y, parent.mnkParameters, lastFactory)
            }
        }
    }


}
