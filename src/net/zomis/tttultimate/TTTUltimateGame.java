package net.zomis.tttultimate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TTTUltimateGame implements Winnable, HasSub<TTBoard> {

	// Game -> Board -> Tile. Clicking tile forces to click a specific board.
	
	private final Collection<TTTile> tilesCollection;
	
	private TTPlayer currentPlayer = TTPlayer.X;
	private TTBoard activeBoard;
	private TTPlayer wonBy = TTPlayer.NONE;
	
	private final TTBoard[][] boards;

	private final int size;
	private final List<TTWinCondition> winConds;
	private StringBuilder history = new StringBuilder();

	private OnMoveListener	onMove;
	
	public TTTUltimateGame() {
		this(3);
	}
	public TTTUltimateGame(int size) {
		this(size, "");
	}
	public TTTUltimateGame(int size, String history) {
		// TODO: m,n,k game: http://en.wikipedia.org/wiki/M,n,k-game
		// TODO: What should extend what? Reduce code duplication. Tile extends Board, or Board extends Tile?
		this.size = size;
		this.boards = new TTBoard[size][size];
		for (int x = 0; x < boards.length; x++) {
			for (int y = 0; y < boards[x].length; y++) {
				boards[x][y] = new TTBoard(this, x, y);
			}
		}
		
		Collection<TTTile> tiles = new ArrayList<>();
		for (TTBoard board : TicUtils.getTiles(this)) {
			for (TTTile tile : TicUtils.getTiles(board)) {
				tiles.add(tile);
			}
		}
		this.tilesCollection = Collections.unmodifiableCollection(tiles);
		this.winConds = TicUtils.setupWins(this);
		
		this.makeMoves(history);
		this.history.append(history);
	}
	
	public boolean isPlayable() {
		return false;
	}
	
	public void makeMoves(String history) throws IllegalStateException, IllegalArgumentException {
		for (String move : history.split(",")) {
			if (move.isEmpty())
				continue;
			if (move.length() != 2)
				throw new IllegalArgumentException("Unexcepted move length. " + move);
			
			int x = Integer.parseInt(String.valueOf(move.charAt(0)), Character.MAX_RADIX);
			int y = Integer.parseInt(String.valueOf(move.charAt(1)), Character.MAX_RADIX);
			
			TTTile tile = this.getTile(x, y);
			if (!tile.playAt())
				throw new IllegalStateException("Unable to make a move at " + x + ", " + y);
		}
	}
	public String saveHistory() {
		return this.history.toString();
	}
	public TTBoard getActiveBoard() {
		return activeBoard;
	}
	public TTPlayer getCurrentPlayer() {
		return currentPlayer;
	}
	
	public void setActiveBoard(TTBoard activeBoard) {
		this.activeBoard = activeBoard;
	}
	void nextPlayer() {
		this.currentPlayer = currentPlayer.next();
	}
	
	@Override
	public TTPlayer getWonBy() {
		return wonBy;
	}
	
	public Collection<TTTile> getAllFields() {
		return tilesCollection;
	}

	public TTTile getTile(int x, int y) {
		if (x < 0 || y < 0)
			return null;
		if (x >= size*size || y >= size*size)
			return null;
		TTBoard board = this.boards[x / size][y / size];
		return board.getSub(x % size, y % size);
	}

	public void checkForWinner() {
		this.wonBy = determineWinner();
	}
	
	private TTPlayer determineWinner() {
		TTPlayer winner = TTPlayer.NONE;
		for (TTWinCondition cond : this.winConds) {
			winner = winner.or(cond.determineWinner());
		}
		return winner;
	}

	public boolean isGameOver() {
		return this.wonBy != TTPlayer.NONE;
	}

	@Override
	public TTBoard getSub(int x, int y) {
		return this.boards[x][y];
	}

	public int getSize() {
		return this.size;
	}
	@Override
	public List<TTWinCondition> getWinConds() {
		return this.winConds;
	}
	public void playedAt(TTTile tile) {
		if (history.length() > 0)
			history.append(",");
		history.append(Integer.toString(tile.getGlobalX(), Character.MAX_RADIX));
		history.append(Integer.toString(tile.getGlobalY(), Character.MAX_RADIX));
		setActiveBoard(boards[tile.getX()][tile.getY()]);
		tile.getBoard().checkForWinner();
		nextPlayer();
		checkForWinner();
		if (onMove != null) {
			onMove.onMove(tile);
		}
	}
	public void setOnMoveListener(OnMoveListener onMove) {
		this.onMove = onMove;
	}
	public void reset() {
		this.wonBy = TTPlayer.NONE;
		for (int xx = 0; xx < getSize(); xx++) {
			for (int yy = 0; yy < getSize(); yy++) {
				this.getSub(xx, yy).reset();
			}
		}
		
		this.currentPlayer = TTPlayer.X;
		this.activeBoard = null;
		this.history = new StringBuilder();
	}
	@Override
	public int getSizeX() {
		return this.getSize();
	}
	@Override
	public int getSizeY() {
		return this.getSize();
	}
	@Override
	public int getConsecutiveRequired() {
		return this.getSize();
	}
}
