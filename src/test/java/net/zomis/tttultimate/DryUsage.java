package net.zomis.tttultimate;

import static org.junit.Assert.*;
import net.zomis.tttultimate.games.TTClassicController;
import net.zomis.tttultimate.games.TTController;
import net.zomis.tttultimate.games.TTUltimateController;

import org.junit.Test;

import com.google.common.collect.Lists;

public class DryUsage {
	private enum Winner implements Winnable {
		NONE, X, O, XO;

		@Override
		public TTPlayer getWonBy() {
			return TTPlayer.valueOf(this.toString());
		}
	}
	
	@Test
	public void consecutivePassed() {
		assertEquals(TTPlayer.NONE, new TTWinCondition(Lists.newArrayList(Winner.NONE, Winner.NONE, Winner.NONE), 2).determineWinnerNew());
		assertEquals(TTPlayer.NONE, new TTWinCondition(Lists.newArrayList(Winner.NONE, Winner.X, Winner.NONE), 2).determineWinnerNew());
		
		assertEquals(TTPlayer.X, TTPlayer.X.and(TTPlayer.XO));
		assertEquals(TTPlayer.X, new TTWinCondition(Lists.newArrayList(Winner.X, Winner.X, Winner.XO), 2).determineWinnerNew());
		assertEquals(TTPlayer.X, new TTWinCondition(Lists.newArrayList(Winner.O, Winner.X, Winner.X, Winner.XO), 2).determineWinnerNew());
		assertEquals(TTPlayer.XO, new TTWinCondition(Lists.newArrayList(Winner.O, Winner.X, Winner.XO, Winner.O), 2).determineWinnerNew());
		assertEquals(TTPlayer.O, new TTWinCondition(Lists.newArrayList(Winner.O, Winner.X, Winner.O, Winner.O, Winner.NONE), 2).determineWinnerNew());
		assertEquals(TTPlayer.XO, new TTWinCondition(Lists.newArrayList(Winner.O, Winner.X, Winner.XO, Winner.XO), 2).determineWinnerNew());
		assertEquals(TTPlayer.XO, new TTWinCondition(Lists.newArrayList(Winner.O, Winner.XO, Winner.X), 2).determineWinnerNew());
		assertEquals(TTPlayer.NONE, new TTWinCondition(Lists.newArrayList(Winner.O, Winner.NONE, Winner.X), 2).determineWinnerNew());
		assertEquals(TTPlayer.XO, new TTWinCondition(Lists.newArrayList(Winner.O, Winner.X), 1).determineWinnerNew());
	}
	@Test
	public void winnerAlgebra() {
		for (TTPlayer pl : TTPlayer.values()) {
			assertEquals(TTPlayer.NONE, TTPlayer.NONE.and(pl));
			assertEquals(pl, TTPlayer.XO.and(pl));
			assertEquals(pl, TTPlayer.NONE.or(pl));
			assertEquals(pl, pl.or(pl));
			assertEquals(pl, pl.and(pl));
			
		}
		assertEquals(TTPlayer.NONE, TTPlayer.X.and(TTPlayer.O));
		assertEquals(TTPlayer.NONE, TTPlayer.O.and(TTPlayer.X));
		assertEquals(TTPlayer.XO, TTPlayer.X.or(TTPlayer.O));
		assertEquals(TTPlayer.XO, TTPlayer.O.or(TTPlayer.X));
	}
	
	@Test
	public void classicTTTeasyWin() {
		TTBase classicTTT = new TTFactories().classicMNK(3, 3, 2);
		TTController classic = new TTClassicController(classicTTT);
		
		assertTrue(classic.play(classicTTT.getSub(1, 1)));
		assertEquals(TTPlayer.X, classicTTT.getSub(1, 1).getWonBy());
		assertTrue(classic.play(classicTTT.getSub(0, 1)));
		assertEquals(TTPlayer.NONE, classicTTT.getWonBy());
		assertTrue(classic.play(classicTTT.getSub(2, 0)));
		assertEquals(TTPlayer.X, classicTTT.getWonBy());
		assertFalse(classic.play(classicTTT.getSub(0, 2)));
	}
	
	@Test
	public void classicTTT() {
		TTBase classicTTT = new TTFactories().classicMNK(3, 3, 3);
		TTController classic = new TTClassicController(classicTTT);
		
		// Create the classic TTT win trap
		assertTrue(classic.play(classicTTT.getSub(1, 1)));
		assertTrue(classic.play(classicTTT.getSub(0, 1)));
		assertTrue(classic.play(classicTTT.getSub(2, 0)));
		assertTrue(classic.play(classicTTT.getSub(0, 2)));
		assertEquals(TTPlayer.NONE, classicTTT.getWonBy());
		assertTrue(classic.play(classicTTT.getSub(0, 0)));
		assertTrue(classic.play(classicTTT.getSub(1, 0)));
		assertTrue(classic.play(classicTTT.getSub(2, 2)));
		assertEquals(TTPlayer.X, classicTTT.getWonBy());
		assertFalse(classic.play(classicTTT.getSub(2, 1)));
	}
	
	@Test
	public void ultimate() {
		TTBase ultimateTTT = new TTFactories().ultimate(3);
		TTUltimateController ultimate = new TTUltimateController(ultimateTTT);
		
		assertTrue(ultimate.play (2, 6));
		assertFalse(ultimate.play(2, 6));
		assertTrue(ultimate.play(8, 2));
		assertTrue(ultimate.play(8, 6));
		assertEquals(TTPlayer.NONE, ultimateTTT.getWonBy());
		assertTrue(ultimate.play(7, 2));
		assertTrue(ultimate.play(5, 6));
		assertEquals(TTPlayer.NONE, ultimateTTT.getSub(2, 0).getWonBy());
		assertTrue(ultimate.play(6, 2));
		assertEquals(TTPlayer.O, ultimateTTT.getSub(2, 0).getWonBy());
	}
	
}
