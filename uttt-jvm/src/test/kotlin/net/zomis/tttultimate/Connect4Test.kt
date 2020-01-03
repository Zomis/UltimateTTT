package net.zomis.tttultimate

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Connect4Test {
    @Test
    fun winnables() {
        val board = TTFactories().classicMNK(7, 6, 4)
        Assertions.assertEquals(7 + 6 + 6 * 2, board.winConds.size)
    }

    @Test
    fun smallestTiles() {
        val game = TTFactories().classicMNK(7, 6, 4)
        Assertions.assertNotNull(game.getSub(6, 5))
        Assertions.assertNotNull(game.getSmallestTile(6, 5))

        Assertions.assertEquals(game.getSub(6, 5), game.getSmallestTile(6, 5))
        for (x in 0..8) {
            for (y in 0..8) {
                Assertions.assertEquals(game.getSub(x, y), game.getSmallestTile(x, y))
            }
        }
    }
}