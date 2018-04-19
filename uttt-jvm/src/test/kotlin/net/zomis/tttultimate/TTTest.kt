package net.zomis.tttultimate

import net.zomis.fight.GameFight
import net.zomis.tttultimate.games.TTControllers
import net.zomis.tttultimate.games.TTOthello
import net.zomis.tttultimate.games.TTQuantumController
import net.zomis.tttultimate.games.TTUltimateController
import net.zomis.tttultimate.players.TTAI
import net.zomis.tttultimate.players.TTAIFactory
import net.zomis.tttultimate.players.TTGeneticAI
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class TTTest {

    @Test
    fun aiFight() {
        val fight = GameFight<TTAI>("AI Fight", false)
        val ais = arrayOf<TTAI>(TTAIFactory.version2().build(), TTAIFactory.version3().build(), TTAIFactory.improved3().build(), TTAIFactory.unreleased().build())
        val results = fight.fightEvenly(ais, 100, TTAIFactory.FightImpl())
        System.out.println(results.toStringMultiLine())
    }

    @Test
    @Disabled
    fun evolve() {
        val genetic = TTGeneticAI()
        genetic.evolve()
        genetic.evolve()
        genetic.evolve()
        genetic.evolve()
    }

    @Test
    fun reversi() {
        val control = TTOthello()
        val game = control.game
        assertTrue(control.play(game.getSub(4, 2)))
        assertFalse(control.isGameOver)
    }

    @Test
    fun connectFour() {
        val control = TTControllers.connectFour()
        val game = control.game
        assertTrue(control.play(game.getSub(3, 5)))
        assertTrue(control.play(game.getSub(0, 5)))
        assertTrue(control.play(game.getSub(3, 4)))
        assertTrue(control.play(game.getSub(0, 4)))
        assertTrue(control.play(game.getSub(3, 3)))
        assertTrue(control.play(game.getSub(0, 3)))
        assertTrue(control.play(game.getSub(3, 2)))
        assertEquals(TTPlayer.X, game.wonBy)
        assertFalse(control.play(game.getSub(4, 5)))
    }

    @Test
    fun entangled() {
        val control = TTQuantumController()
        val game = control.game

        assertTrue(control.play(0, 0))
        assertFalse(control.play(0, 1))
        assertTrue(control.play(3, 0))
        assertFalse(control.play(0, 0))

        assertTrue(control.play(4, 0))
        assertTrue(control.play(3, 3))
        assertFalse(control.play(0, 0))

        assertTrue(control.play(1, 0))
        assertTrue(control.play(4, 3))
        assertFalse(control.play(0, 0))
        assertTrue(control.play(1, 0))

        assertEquals(TTPlayer.X, game.getSub(1, 0)!!.wonBy)
        assertEquals(TTPlayer.O, game.getSub(1, 1)!!.wonBy)
        assertEquals(TTPlayer.X, game.getSub(0, 0)!!.wonBy)

        control.reset()
        control.makeMoves("00,30,40,33,10,43")
        assertTrue(control.play(4, 3))
        assertEquals(TTPlayer.X, game.getSub(0, 0)!!.wonBy)
        assertEquals(TTPlayer.O, game.getSub(1, 0)!!.wonBy)
        assertEquals(TTPlayer.X, game.getSub(1, 1)!!.wonBy)
    }

    @Test
    fun cannotPlayWonBoard() {
        val game = TTFactories().ultimate()
        val control = TTUltimateController(game)

        assertEquals(TTPlayer.O, TTPlayer.NONE.or(TTPlayer.O))
        assertEquals(TTPlayer.X, TTPlayer.X.or(TTPlayer.X))
        assertEquals(TTPlayer.NONE, TTPlayer.NONE.or(TTPlayer.NONE))
        assertEquals(TTPlayer.XO, TTPlayer.X.or(TTPlayer.XO))

        System.out.println(game.winConds)

        assertTrue(control.play(2, 6))
        assertFalse(control.play(2, 6))
        assertTrue(control.play(8, 2))
        assertTrue(control.play(8, 6))
        assertTrue(control.play(7, 2))
        assertTrue(control.play(5, 6))
        assertTrue(control.play(6, 2))
        assertEquals(TTPlayer.O, game.getSub(2, 0)!!.wonBy)

        assertTrue(control.play(1, 7))
        assertTrue(control.play(5, 3))

        assertTrue(control.isAllowedPlay(game.getSmallestTile(0, 0)!!))
        assertFalse(control.play(8, 0))
        assertTrue(control.play(0, 8))

        assertTrue(control.play(4, 5))
        assertTrue(control.play(4, 6))
        assertFalse(control.play(8, 0))
        assertFalse(control.play(1, 1))
        assertFalse(control.play(7, 4))
        assertTrue(control.play(4, 2))
        assertTrue(control.play(3, 6))
        assertTrue(control.play(2, 2))
        assertTrue(control.play(7, 6))
        assertTrue(control.play(5, 2))
        assertEquals(TTPlayer.NONE, game.wonBy)
        assertTrue(control.play(6, 6))
        assertEquals(TTPlayer.X, game.wonBy)
    }

    @Test
    fun historyMove() {
        val game = TTFactories().ultimate()
        val controller = TTUltimateController(game)
        controller.makeMoves("44,33,11,34,25,88,87,74,53,71,43,41,45,47")
        assertEquals(TTPlayer.X, game.getSub(1, 1)!!.wonBy)
    }

    @Test
    fun smallestTiles() {
        val game = TTFactories().ultimate()
        assertEquals(game.getSub(2, 1)!!.getSub(0, 2), game.getSmallestTile(6, 5))
        for (x in 0..8) {
            for (y in 0..8) {
                assertEquals(game.getSub(x / 3, y / 3)!!.getSub(x % 3, y % 3), game.getSmallestTile(x, y))
            }
        }
    }

    @Test
    fun reset() {
        val game = TTFactories().ultimate()
        val controller = TTUltimateController(game)
        controller.makeMoves("44,33,11,34,25,88,87,74,53,71,43,41,45,47")
        assertEquals(TTPlayer.X, game.getSub(1, 1)!!.wonBy)

        controller.reset()
        controller.makeMoves("44,33,11,34,25,88,87,74,53,71,43,41,45,47")
        assertEquals(TTPlayer.X, game.getSub(1, 1)!!.wonBy)
    }

    @Test
    fun quantumTieBreaker() {
        val controller = TTQuantumController()
        controller.makeMoves("00,30,03,33,01,31,01,60,63,66,43,13,16,36,70,73,53,23,06")
        assertEquals(TTPlayer.NONE, controller.wonBy)
        assertTrue(controller.play(0, 6))
        assertEquals(TTPlayer.O, controller.wonBy)
    }

    @Test
    fun winTest() {
        val game = TTFactories().ultimate()
        val controller = TTUltimateController(game)
        controller.makeMoves("44,33,11,43,41,53")
        assertEquals(TTPlayer.O, game.getSub(1, 1)!!.wonBy)
    }

}
