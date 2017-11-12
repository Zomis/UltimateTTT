package net.zomis.tttultimate;

import static org.junit.Assert.*;
import net.zomis.fight.FightResults;
import net.zomis.fight.GameFight;
import net.zomis.tttultimate.games.TTController;
import net.zomis.tttultimate.games.TTControllers;
import net.zomis.tttultimate.games.TTOthello;
import net.zomis.tttultimate.games.TTQuantumController;
import net.zomis.tttultimate.games.TTUltimateController;
import net.zomis.tttultimate.players.TTAI;
import net.zomis.tttultimate.players.TTAIFactory;
import net.zomis.tttultimate.players.TTGeneticAI;

import org.junit.Ignore;
import org.junit.Test;

public class TTTest {

	@Test 	@Ignore
	public void doNothing() {	}
	
	@Test
	public void aiFight() {
		GameFight<TTAI> fight = new GameFight<>("AI Fight", false);
		TTAI[] ais = new TTAI[]{ TTAIFactory.version2().build(), TTAIFactory.version3().build(), 
				TTAIFactory.improved3().build(), TTAIFactory.unreleased().build() };
		FightResults<TTAI> results = fight.fightEvenly(ais, 100, new TTAIFactory.FightImpl());
		System.out.println(results.toStringMultiLine());
	}
	
	@Test
	@Ignore
	public void evolve() {
		TTGeneticAI genetic = new TTGeneticAI();
		genetic.evolve();
		genetic.evolve();
		genetic.evolve();
		genetic.evolve();
	}
	
	@Test
	public void reversi() {
		TTController control = new TTOthello();
		TTBase game = control.getGame();
		assertTrue(control.play(game.getSub(4, 2)));
		assertFalse(control.isGameOver());
	}
	@Test
	public void connectFour() {
		TTController control = TTControllers.connectFour();
		TTBase game = control.getGame();
		assertTrue(control.play(game.getSub(3, 5)));
		assertTrue(control.play(game.getSub(0, 5)));
		assertTrue(control.play(game.getSub(3, 4)));
		assertTrue(control.play(game.getSub(0, 4)));
		assertTrue(control.play(game.getSub(3, 3)));
		assertTrue(control.play(game.getSub(0, 3)));
		assertTrue(control.play(game.getSub(3, 2)));
		assertEquals(TTPlayer.X, game.getWonBy());
		assertFalse(control.play(game.getSub(4, 5)));
	}
	
	@Test
	public void entangled() {
		TTQuantumController control = new TTQuantumController();
		TTBase game = control.getGame();
		
		assertTrue(control.play(0, 0));
		assertFalse(control.play(0, 1));
		assertTrue(control.play(3, 0));
		assertFalse(control.play(0, 0));
		
		assertTrue(control.play(4, 0));
		assertTrue(control.play(3, 3));
		assertFalse(control.play(0, 0));
		
		assertTrue(control.play(1, 0));
		assertTrue(control.play(4, 3));
		assertFalse(control.play(0, 0));
		assertTrue(control.play(1, 0));
		
		assertEquals(TTPlayer.X, game.getSub(1, 0).getWonBy());
		assertEquals(TTPlayer.O, game.getSub(1, 1).getWonBy());
		assertEquals(TTPlayer.X, game.getSub(0, 0).getWonBy());
		
		control.reset();
		control.makeMoves("00,30,40,33,10,43");
		assertTrue(control.play(4, 3));
		assertEquals(TTPlayer.X, game.getSub(0, 0).getWonBy());
		assertEquals(TTPlayer.O, game.getSub(1, 0).getWonBy());
		assertEquals(TTPlayer.X, game.getSub(1, 1).getWonBy());
	}
		
	@Test
	public void cannotPlayWonBoard() {
		TTBase game = new TTFactories().ultimate();
		TTController control = new TTUltimateController(game);
		
		assertEquals(TTPlayer.O, TTPlayer.NONE.or(TTPlayer.O));
		assertEquals(TTPlayer.X, TTPlayer.X.or(TTPlayer.X));
		assertEquals(TTPlayer.NONE, TTPlayer.NONE.or(TTPlayer.NONE));
		assertEquals(TTPlayer.XO, TTPlayer.X.or(TTPlayer.XO));
		
		System.out.println(game.getWinConds());
		
		assertTrue(control.play(2, 6));
		assertFalse(control.play(2, 6));
		assertTrue(control.play(8, 2));
		assertTrue(control.play(8, 6));
		assertTrue(control.play(7, 2));
		assertTrue(control.play(5, 6));
		assertTrue(control.play(6, 2));
		assertEquals(TTPlayer.O, game.getSub(2, 0).getWonBy());
		
		assertTrue(control.play(1, 7));
		assertTrue(control.play(5, 3));
		
		assertTrue(control.isAllowedPlay(game.getSmallestTile(0, 0)));
		assertFalse(control.play(8, 0));
		assertTrue(control.play(0, 8));
		
		assertTrue(control.play(4, 5));
		assertTrue(control.play(4, 6));
		assertFalse(control.play(8, 0));
		assertFalse(control.play(1, 1));
		assertFalse(control.play(7, 4));
		assertTrue(control.play(4, 2));
		assertTrue(control.play(3, 6));
		assertTrue(control.play(2, 2));
		assertTrue(control.play(7, 6));
		assertTrue(control.play(5, 2));
		assertEquals(TTPlayer.NONE, game.getWonBy());
		assertTrue(control.play(6, 6));
		assertEquals(TTPlayer.X, game.getWonBy());
	}
	
	@Test
	public void historyMove() {
		TTBase game = new TTFactories().ultimate();
		TTController controller = new TTUltimateController(game);
		controller.makeMoves("44,33,11,34,25,88,87,74,53,71,43,41,45,47");
		assertEquals(TTPlayer.X, game.getSub(1, 1).getWonBy());
	}
	
	@Test
	public void reset() {
		TTBase game = new TTFactories().ultimate();
		TTUltimateController controller = new TTUltimateController(game);
		controller.makeMoves("44,33,11,34,25,88,87,74,53,71,43,41,45,47");
		assertEquals(TTPlayer.X, game.getSub(1, 1).getWonBy());
		
		controller.reset();
		controller.makeMoves("44,33,11,34,25,88,87,74,53,71,43,41,45,47");
		assertEquals(TTPlayer.X, game.getSub(1, 1).getWonBy());
	}
	
	@Test
	public void quantumTieBreaker() {
		TTController controller = new TTQuantumController();
		controller.makeMoves("00,30,03,33,01,31,01,60,63,66,43,13,16,36,70,73,53,23,06");
		assertEquals(TTPlayer.NONE, controller.getWonBy());
		assertTrue(controller.play(0, 6));
		assertEquals(TTPlayer.O, controller.getWonBy());
	}
	
	@Test
	public void winTest() {
		TTBase game = new TTFactories().ultimate();
		TTController controller = new TTUltimateController(game);
		controller.makeMoves("44,33,11,43,41,53");
		assertEquals(TTPlayer.O, game.getSub(1, 1).getWonBy());
	}
	
}
