package test.net.zomis.tttultimate;

import static org.junit.Assert.*;
import net.zomis.fight.FightResults;
import net.zomis.fight.GameFight;
import net.zomis.tttultimate.TTPlayer;
import net.zomis.tttultimate.TTTUltimateGame;
import net.zomis.tttultimate.TTTile;
import net.zomis.tttultimate.players.TTAI;
import net.zomis.tttultimate.players.TTAIFactory;
import net.zomis.tttultimate.players.TTGeneticAI;
import net.zomis.tttultimate.players.TTHuman;

import org.junit.Ignore;
import org.junit.Test;

import com.google.common.base.Strings;

public class TTTest {

	private final TTAI ai = TTAIFactory.version3().build();
	
	@Test 	@Ignore
	public void doNothing() {	}
	
	@Test
	public void aiFight() {
		GameFight<TTAI> fight = new GameFight<>("AI Fight", false);
		TTAI[] ais = new TTAI[]{ TTAIFactory.version2().build(), TTAIFactory.version3().build(), 
				TTAIFactory.improved3().build(), TTAIFactory.unreleased().build() };
		FightResults<TTAI> results = fight.fightEvenly(ais, 1000, new TTAIFactory.FightImpl());
		System.out.println(results.toStringMultiLine());
	}
	private final TTAI logAI = TTAIFactory.best().build();
	
//	@Test
	public void evolve() {
		TTGeneticAI genetic = new TTGeneticAI();
		genetic.evolve();
		genetic.evolve();
		genetic.evolve();
		genetic.evolve();
	}
	
	@Test
	@Ignore
	public void humanInput() {
		TTHuman human = new TTHuman();
		TTTUltimateGame game = new TTTUltimateGame();
		while (!game.isGameOver()) {
			output(game);
			human.play(game).playAt();
			output(game);
			
			logAI.logScores(game);
			
			TTTile tile = ai.play(game);
			if (tile == null)
				continue;
			tile.playAt();
			System.out.println("AI Played at " + tile.getGlobalX() + ", " + tile.getGlobalY());
		}
		human.close();
	}
	
	public void output(TTTUltimateGame game) {
		final int RADIX = Character.MAX_RADIX;
		final int quad = game.getSize() * game.getSize();
		final int size = game.getSize();
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
				TTTile tile = game.getTile(x, y);
				out.append(tile.getCharOutput());
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
	
	@Test
	public void cannotPlayWonBoard() {
		TTTUltimateGame game = new TTTUltimateGame();
//		game.output();
		if (game.getSize() != 3)
			return;
		
		assertEquals(TTPlayer.O, TTPlayer.NONE.or(TTPlayer.O));
		assertEquals(TTPlayer.X, TTPlayer.X.or(TTPlayer.X));
		assertEquals(TTPlayer.NONE, TTPlayer.NONE.or(TTPlayer.NONE));
		assertEquals(TTPlayer.XO, TTPlayer.X.or(TTPlayer.XO));
		
		assertTrue(game.getTile(2, 6).playAt());
		assertFalse(game.getTile(2, 6).isPlayable());
		assertTrue(game.getTile(8, 2).playAt());
		assertTrue(game.getTile(8, 6).playAt());
		assertTrue(game.getTile(7, 2).playAt());
		assertTrue(game.getTile(5, 6).playAt());
		assertTrue(game.getTile(6, 2).playAt());
		assertEquals(TTPlayer.O, game.getSub(2, 0).getWonBy());
		
		assertTrue(game.getTile(1, 7).playAt());
		assertTrue(game.getTile(5, 3).playAt());
		
		assertTrue(game.getTile(0, 0).isPlayable());
		assertFalse(game.getTile(8, 0).isPlayable());
		assertTrue(game.getTile(0, 8).playAt());
		
		assertTrue(game.getTile(4, 5).playAt());
		assertTrue(game.getTile(4, 6).playAt());
		assertFalse(game.getTile(8, 0).isPlayable());
		assertFalse(game.getTile(1, 1).isPlayable());
		assertFalse(game.getTile(7, 4).isPlayable());
		assertTrue(game.getTile(4, 2).playAt());
		assertTrue(game.getTile(3, 6).playAt());
		assertTrue(game.getTile(2, 2).playAt());
		assertTrue(game.getTile(7, 6).playAt());
		assertTrue(game.getTile(5, 2).playAt());
		assertEquals(TTPlayer.NONE, game.getWonBy());
		assertTrue(game.getTile(6, 6).playAt());
		assertEquals(TTPlayer.X, game.getWonBy());
	}
	
	@Test
	public void historyMove() {
		TTTUltimateGame game = new TTTUltimateGame();
		game.makeMoves("44,33,11,34,25,88,87,74,53,71,43,41,45,47");
		assertEquals(TTPlayer.X, game.getSub(1, 1).getWonBy());
	}
	
	@Test
	public void reset() {
		TTTUltimateGame game = new TTTUltimateGame();
		game.makeMoves("44,33,11,34,25,88,87,74,53,71,43,41,45,47");
		assertEquals(TTPlayer.X, game.getSub(1, 1).getWonBy());
		
		game.reset();
		game.makeMoves("44,33,11,34,25,88,87,74,53,71,43,41,45,47");
		assertEquals(TTPlayer.X, game.getSub(1, 1).getWonBy());
	}
	
	@Test
	public void winTest() {
		TTTUltimateGame game = new TTTUltimateGame();
		game.makeMoves("44,33,11,43,41,53");
		assertEquals(TTPlayer.O, game.getSub(1, 1).getWonBy());
	}
	
}
