package net.zomis.tttultimate;

import java.util.Collection;
import java.util.List;



public class TTBoard implements Winnable, HasSub<TTTile> {
	private final TTTile[][] tiles;
	private final TTTUltimateGame game;

	private final int x;
	private final int y;
	private final List<TTWinCondition> winConds;
	
	private TTPlayer wonBy = TTPlayer.NONE;
	
	public TTBoard(TTTUltimateGame game, int xx, int yy) {
		this.tiles = new TTTile[game.getSize()][game.getSize()];
		this.x = xx;
		this.y = yy;
		this.game = game;
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[x].length; y++) {
				tiles[x][y] = new TTTile(this, x, y);
			}
		}
		this.winConds = TicUtils.setupWins(this);
	}
	
	@Override
	public int getSize() {
		return this.game.getSize();
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public void setWonBy(TTPlayer wonBy) {
		this.wonBy = wonBy;
	}
	@Override
	public TTPlayer getWonBy() {
		return wonBy;
	}
	
	public TTTUltimateGame getGame() {
		return game;
	}

	public void checkForWinner() {
		this.wonBy = determineWinner();
	}
	private TTPlayer determineWinner() {
		for (TTWinCondition cond : this.winConds) {
			TTPlayer winner = cond.determineWinner();
			if (TTPlayer.isRealPlayer(winner))
				return winner;
		}
		return TTPlayer.NONE;
	}

	@Override
	public TTTile getSub(int x, int y) {
		return this.tiles[x][y];
	}

	@Override
	public Collection<TTWinCondition> getWinConds() {
		return this.winConds;
	}

	void reset() {
		this.wonBy = TTPlayer.NONE;
		for (int xx = 0; xx < getSize(); xx++) {
			for (int yy = 0; yy < getSize(); yy++) {
				this.getSub(xx, yy).reset();
			}
		}
	}

}
