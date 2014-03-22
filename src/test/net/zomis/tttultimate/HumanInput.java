package test.net.zomis.tttultimate;

import net.zomis.tttultimate.TTBase;
import net.zomis.tttultimate.TTFactories;
import net.zomis.tttultimate.TTPlayer;
import net.zomis.tttultimate.games.TTController;
import net.zomis.tttultimate.games.TTUltimateController;
import net.zomis.tttultimate.players.TTAI;
import net.zomis.tttultimate.players.TTAIFactory;
import net.zomis.tttultimate.players.TTHuman;

import org.junit.Ignore;
import org.junit.Test;

import com.google.common.base.Strings;

public class HumanInput {
	private final TTAI ai = TTAIFactory.version3().build();
	final TTAI logAI = TTAIFactory.best().build();
	
	@Test
	@Ignore
	public void humanInput() {
		TTHuman human = new TTHuman();
		TTController controller = new TTUltimateController(new TTFactories().ultimate());
		while (!controller.isGameOver()) {
			output(controller);
			controller.play(human.play(controller));
			output(controller);
			
//			logAI.logScores(game);
			TTBase tile = ai.play(controller);
			if (tile == null)
				continue;
			controller.play(tile);
//			System.out.println("AI Played at " + tile.getGlobalX() + ", " + tile.getGlobalY());
		}
		human.close();
	}
	
	public void output(TTController game) {
		final int RADIX = Character.MAX_RADIX;
		final int quad = game.getGame().getSizeX() * game.getGame().getSizeY();
		final int size = game.getGame().getSizeX();
		final String pre = "    ";
		
		System.out.print(pre + "  ");
		for (int i = 0; i < quad; i++) {
			if (i > 0 && i % size == 0)
				System.out.print(' ');
			System.out.print(Integer.toString(i, RADIX));
		}
		System.out.println("");
		System.out.print(pre + " " + Strings.repeat("-", quad + size + 1));
		System.out.println("");
		for (int y = 0; y < quad; y++) {
			StringBuilder out = new StringBuilder(pre);
			for (int x = 0; x < quad; x++) {
				if (x == 0) {
					out.append(Integer.toString(y, RADIX));
					out.append('|');
				}
				TTBase tile = game.getGame().getSmallestTile(x, y);
				out.append(getCharOutput(game, tile));
				if (x % size == size - 1)
					out.append('|');
			}
			System.out.println(out);
			if (y % size == size - 1)
				System.out.println(pre + " " + Strings.repeat("-", quad + size + 1));
		}
		System.out.print(pre);
		if (game.isGameOver())
			System.out.println("Game is won by " + game.getWonBy());
		else System.out.println("Current player is " + game.getCurrentPlayer());
		System.out.println();
	}
	
	public static char getCharOutput(TTController controller, TTBase tile) {
		TTPlayer player = tile.getWonBy();
		if (player == null || player == TTPlayer.NONE)
			return controller.isAllowedPlay(tile) ? '.' : ' ';
		if (player == TTPlayer.XO)
			return '?';
		TTPlayer winner = tile.getParent().getWonBy();
		return TTPlayer.isExactlyOnePlayer(winner) && !winner.equals(player) 
				? '-' : player.toString().charAt(0);
	}
	
}
