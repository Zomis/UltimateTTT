package net.zomis.tttultimate

import net.zomis.tttultimate.games.TTClassicController
import net.zomis.tttultimate.games.TTUltimateController
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DryUsage {
    private enum class Winner : Winnable {
        NONE, X, O, XO;

        override val wonBy: TTPlayer
            get() = TTPlayer.valueOf(this.toString())
    }

    @Test
    fun consecutivePassed() {
        assertEquals(TTPlayer.NONE, TTWinCondition(listOf(Winner.NONE, Winner.NONE, Winner.NONE), 2).determineWinnerNew())
        assertEquals(TTPlayer.NONE, TTWinCondition(listOf(Winner.NONE, Winner.X, Winner.NONE), 2).determineWinnerNew())

        assertEquals(TTPlayer.X, TTPlayer.X.and(TTPlayer.XO))
        assertEquals(TTPlayer.X, TTWinCondition(listOf(Winner.X, Winner.X, Winner.XO), 2).determineWinnerNew())
        assertEquals(TTPlayer.X, TTWinCondition(listOf(Winner.O, Winner.X, Winner.X, Winner.XO), 2).determineWinnerNew())
        assertEquals(TTPlayer.XO, TTWinCondition(listOf(Winner.O, Winner.X, Winner.XO, Winner.O), 2).determineWinnerNew())
        assertEquals(TTPlayer.O, TTWinCondition(listOf(Winner.O, Winner.X, Winner.O, Winner.O, Winner.NONE), 2).determineWinnerNew())
        assertEquals(TTPlayer.XO, TTWinCondition(listOf(Winner.O, Winner.X, Winner.XO, Winner.XO), 2).determineWinnerNew())
        assertEquals(TTPlayer.XO, TTWinCondition(listOf(Winner.O, Winner.XO, Winner.X), 2).determineWinnerNew())
        assertEquals(TTPlayer.NONE, TTWinCondition(listOf(Winner.O, Winner.NONE, Winner.X), 2).determineWinnerNew())
        assertEquals(TTPlayer.XO, TTWinCondition(listOf(Winner.O, Winner.X), 1).determineWinnerNew())
    }

    @Test
    fun winnerAlgebra() {
        for (pl in TTPlayer.values()) {
            assertEquals(TTPlayer.NONE, TTPlayer.NONE.and(pl))
            assertEquals(pl, TTPlayer.XO.and(pl))
            assertEquals(pl, TTPlayer.NONE.or(pl))
            assertEquals(pl, pl.or(pl))
            assertEquals(pl, pl.and(pl))

        }
        assertEquals(TTPlayer.NONE, TTPlayer.X.and(TTPlayer.O))
        assertEquals(TTPlayer.NONE, TTPlayer.O.and(TTPlayer.X))
        assertEquals(TTPlayer.XO, TTPlayer.X.or(TTPlayer.O))
        assertEquals(TTPlayer.XO, TTPlayer.O.or(TTPlayer.X))
    }

    @Test
    fun classicTTTeasyWin() {
        val classicTTT = TTFactories().classicMNK(3, 3, 2)
        val classic = TTClassicController(classicTTT)

        assertTrue(classic.play(classicTTT.getSub(1, 1)))
        assertEquals(TTPlayer.X, classicTTT.getSub(1, 1)!!.wonBy)
        assertTrue(classic.play(classicTTT.getSub(0, 1)))
        assertEquals(TTPlayer.NONE, classicTTT.wonBy)
        assertTrue(classic.play(classicTTT.getSub(2, 0)))
        assertEquals(TTPlayer.X, classicTTT.wonBy)
        assertFalse(classic.play(classicTTT.getSub(0, 2)))
    }

    @Test
    fun classicTTT() {
        val classicTTT = TTFactories().classicMNK(3, 3, 3)
        val classic = TTClassicController(classicTTT)

        // Create the classic TTT win trap
        assertTrue(classic.play(classicTTT.getSub(1, 1)))
        assertTrue(classic.play(classicTTT.getSub(0, 1)))
        assertTrue(classic.play(classicTTT.getSub(2, 0)))
        assertTrue(classic.play(classicTTT.getSub(0, 2)))
        assertEquals(TTPlayer.NONE, classicTTT.wonBy)
        assertTrue(classic.play(classicTTT.getSub(0, 0)))
        assertTrue(classic.play(classicTTT.getSub(1, 0)))
        assertTrue(classic.play(classicTTT.getSub(2, 2)))
        assertEquals(TTPlayer.X, classicTTT.wonBy)
        assertFalse(classic.play(classicTTT.getSub(2, 1)))
    }

    @Test
    fun ultimate() {
        val ultimateTTT = TTFactories().ultimate(3)
        val ultimate = TTUltimateController(ultimateTTT)

        assertTrue(ultimate.play(2, 6))
        assertFalse(ultimate.play(2, 6))
        assertTrue(ultimate.play(8, 2))
        assertTrue(ultimate.play(8, 6))
        assertEquals(TTPlayer.NONE, ultimateTTT.wonBy)
        assertTrue(ultimate.play(7, 2))
        assertTrue(ultimate.play(5, 6))
        assertEquals(TTPlayer.NONE, ultimateTTT.getSub(2, 0)!!.wonBy)
        assertTrue(ultimate.play(6, 2))
        assertEquals(TTPlayer.O, ultimateTTT.getSub(2, 0)!!.wonBy)
    }

}
