package net.zomis.tttultimate.players;

import java.util.Scanner;

import net.zomis.tttultimate.TTBase;
import net.zomis.tttultimate.games.TTController;

public class TTHuman {

	private Scanner sc = new Scanner(System.in);

	public TTBase play(TTController game) {
		TTBase chosenTile = null;
		do {
//			TTBase active = game.getActiveBoard();
//			if (active != null) {
//				int xx = active.getX();
//				int yy = active.getY();
//				int size = game.getSize();
//				System.out.printf("Active board is within coordinates %d-%d, %d-%d", 
//						xx * size, xx * size + size - 1, 
//						yy * size, yy * size + size - 1);
//				System.out.println();
//			}
//			System.out.print("Input x coordinate: ");
//			int x = input(sc, game);
//			System.out.print("Input y coordinate: ");
//			int y = input(sc, game);
//			chosenTile = game.getTile(x, y);
//			System.out.println("Chosen tile is " + chosenTile + " in board " + game + " playable " + (chosenTile == null ? "null" : chosenTile.isPlayable()));
		}
		while (chosenTile == null || !game.isAllowedPlay(chosenTile));
		
		return chosenTile;
	}
	
	private final TTAI logAI = TTAIFactory.best().build();
	
	private int input(Scanner sc, TTController board) {
		String next = null;
		int result = -1;
		do {
			next = sc.next();
			result = handleInput(next);
			if (next.equals("S")) {
//				logAI.logScores(board); // TODO logAI.logScores
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
