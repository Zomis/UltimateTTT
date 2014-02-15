package net.zomis.tttultimate.dry;

import net.zomis.tttultimate.TTPlayer;

public abstract class TTController {
	
	private final TTBase game;

	public TTController(TTBase board) {
		this.game = board;
	}

	protected TTPlayer currentPlayer = TTPlayer.X;
	
	public abstract boolean isAllowedPlay(TTBase tile);
	
	public final boolean play(TTBase tile) {
		if (!isAllowedPlay(tile)) {
			System.out.println("Warning: Move was not made. Unable to play at " + tile);
			return false;
		}
		
		boolean success = this.performPlay(tile);
		// Check for win condition on tile and if there is a win, cascade to it's parents
		do {
			tile = tile.determineWinner() == null ? null : tile.getParent();
		}
		while (tile != null);
		return success;
	}
	
	protected abstract boolean performPlay(TTBase tile);
	
	public boolean play(int x, int y) {
		return this.play(game.getSmallestTile(x, y));
	}
	
	protected abstract void onPlay(TTBase tile);
	
	public TTPlayer getCurrentPlayer() {
		return currentPlayer;
	}
	
	protected void nextPlayer() {
		currentPlayer = currentPlayer.next();
	}
	
	protected TTBase getGame() {
		return game;
	}
	
}
