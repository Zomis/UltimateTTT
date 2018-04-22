package net.zomis.tttultimate

import net.zomis.tttultimate.games.TTController
import net.zomis.tttultimate.games.TTUltimateController
import net.zomis.tttultimate.players.TTAI
import net.zomis.tttultimate.players.TTAIFactory
import net.zomis.tttultimate.players.TTHuman
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class HumanInput {
    private val ai = TTAIFactory.version3().build()
    internal val logAI = TTAIFactory.best().build()

    @Test
    @Disabled
    fun humanInput() {
        val human = TTHuman()
        val controller = TTUltimateController(TTFactories().ultimate(3))
        while (!controller.isGameOver) {
            output(controller)
            controller.play(human.play(controller))
            output(controller)

            //			logAI.logScores(game);
            val tile = ai.play(controller) ?: continue
            controller.play(tile)
            //			System.out.println("AI Played at " + tile.getGlobalX() + ", " + tile.getGlobalY());
        }
        human.close()
    }

    fun output(game: TTController) {
        val RADIX = Character.MAX_RADIX
        val quad = game.game.sizeX * game.game.sizeY
        val size = game.game.sizeX
        val pre = "    "

        print("$pre  ")
        for (i in 0 until quad) {
            if (i > 0 && i % size == 0)
                print(' ')
            print(Integer.toString(i, RADIX))
        }
        println("")
        print(pre + " " + "-".repeat(quad + size + 1))
        println("")
        for (y in 0 until quad) {
            val out = StringBuilder(pre)
            for (x in 0 until quad) {
                if (x == 0) {
                    out.append(Integer.toString(y, RADIX))
                    out.append('|')
                }
                val tile = game.game.getSmallestTile(x, y)
                out.append(getCharOutput(game, tile!!))
                if (x % size == size - 1)
                    out.append('|')
            }
            println(out)
            if (y % size == size - 1)
                println(pre + " " + "-".repeat(quad + size + 1))
        }
        print(pre)
        if (game.isGameOver)
            println("Game is won by " + game.wonBy)
        else
            println("Current player is " + game.currentPlayer)
        println()
    }

    companion object {

        fun getCharOutput(controller: TTController, tile: TTBase): Char {
            val player = tile.wonBy
            if (player == null || player === TTPlayer.NONE)
                return if (controller.isAllowedPlay(tile)) '.' else ' '
            if (player === TTPlayer.XO)
                return '?'
            val winner = tile.parent!!.wonBy
            return if (TTPlayer.isExactlyOnePlayer(winner) && !winner.equals(player))
                '-'
            else
                player.toString()[0]
        }
    }

}
