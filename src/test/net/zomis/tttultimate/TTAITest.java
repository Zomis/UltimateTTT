package test.net.zomis.tttultimate;

import static org.junit.Assert.*;
import net.zomis.tttultimate.TTBase;
import net.zomis.tttultimate.TTFactories;
import net.zomis.tttultimate.games.TTClassicController;
import net.zomis.tttultimate.games.TTController;
import net.zomis.tttultimate.games.TTUltimateController;
import net.zomis.tttultimate.players.TTAI;
import net.zomis.tttultimate.players.TTAIFactory;

import org.junit.Before;
import org.junit.Test;

public class TTAITest {

	private TTAI	ai;
	private TTBase game;
	private TTController controller;

	@Before
	public void createAI() {
		ai = TTAIFactory.best().build();
	}
	
	@Test
	public void possiblyBadMove() {
		// TODO: Where you send your opponent seems to be very important. Don't send opponent to an important square you want to win, also don't send to a square he's about to win.
		// TODO: A possible: "Will opponent be able to win if I play here"-scorer (also an analyze for where the most important squares are at the moment?)
		// many bad moves: 44,55,77,53,82,66,20,62,08,17,54,74,34,05,06,00,22,86,80,61,13,51,75,58,76,42,57,64,15,46,40,52,78,37,04,23,81,84,21
		loadGame("44,55,77,53,82,66,20,62,08,17,54");
		assertNotMove(game.getSmallestTile(7, 4));
	}
	
	@Test
	public void winIsBetterThanPreventLose() {
		game = new TTFactories().classicMNK(3);
		controller = new TTClassicController(game);
		controller.makeMoves("11,21,20,02,00,22");
		assertMove(game.getSub(0, 0).getSub(1, 0));
	}
	
	private void assertNotMove(TTBase tile) {
		assertNotSame(tile, ai.play(controller));
	}

	@Test
	public void winWhenYouCan() {
		loadGame("44,55,77,53,62,17,54,63,00,02,28,78,56,61,25,68,26,81,74,33,10,40,30,20,71,43,31,15,38,27,75,37,23,82,88,66,12,57,73,42,47,67,24");
		assertMove(game.getSmallestTile(1, 1));
	}

	private void assertMove(TTBase tile) {
		assertEquals(tile, ai.play(controller));
	}

	private TTController loadGame(String history) {
		game = new TTFactories().ultimate();
		controller = new TTUltimateController(game);
		controller.makeMoves(history);
		return controller;
	}
}
