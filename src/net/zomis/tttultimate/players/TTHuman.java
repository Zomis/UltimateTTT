package net.zomis.tttultimate.players;

import java.util.Scanner;

import net.zomis.tttultimate.TTBoard;
import net.zomis.tttultimate.TTTUltimateGame;
import net.zomis.tttultimate.TTTile;

public class TTHuman {

	private Scanner sc = new Scanner(System.in);

	public TTTile play(TTTUltimateGame board) {
		TTTile chosen = null;
		do {
//			board.output();
			TTBoard active = board.getActiveBoard();
			if (active != null) {
				int xx = active.getX();
				int yy = active.getY();
				int size = board.getSize();
				System.out.printf("Active board is within coordinates %d-%d, %d-%d", 
						xx * size, xx * size + size - 1, 
						yy * size, yy * size + size - 1);
				System.out.println();
			}
			System.out.print("Input x coordinate: ");
			int x = input(sc, board);
			System.out.print("Input y coordinate: ");
			int y = input(sc, board);
			chosen = board.getTile(x, y);
			System.out.println("Chosen tile is " + chosen + " in board " + board + " playable " + (chosen == null ? "XXX" : chosen.isPlayable()));
		}
		while (chosen == null || !chosen.isPlayable());
		
		return chosen;
	}
	
	private final TTAI logAI = TTAIFactory.best().build();
	
	private int input(Scanner sc, TTTUltimateGame board) {
		String next = null;
		int result = -1;
		do {
			next = sc.next();
			result = handleInput(next);
			if (next.equals("S")) {
				logAI.logScores(board);
			}
			else if (next.equals("P")) {
				// Find out global status of the entire game
			}
		}
		while (result == -1);
		return result;
	}

	private int handleInput(String input) {
		try {
			return Integer.parseInt(input, Character.MAX_RADIX);
		}
		catch (NumberFormatException e) {
			return -1;
		}
	}

	public void close() {
		sc.close();
	}

}
