package net.zomis.tttultimate;


public class TTTile implements Winnable {
	private final TTBoard board;
	private TTPlayer playedBy = TTPlayer.NONE;
	private int	x;
	private int	y;
	
	public TTTile(TTBoard board, int x, int y) {
		this.board = board;
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
	public boolean isPlayable() {
		if (playedBy != null && playedBy != TTPlayer.NONE)
			return false;
		if (board.getWonBy().isRealPlayer())
			return false;
		if (board.getGame().isGameOver())
			return false;
		TTBoard active = board.getGame().getActiveBoard();
		return active == null || active == board || active.getWonBy() != TTPlayer.NONE;
	}
	
	public boolean playAt() {
		if (!isPlayable()) {
			System.out.println("Warning: Move was not made. Unable to play at " + this);
			return false;
		}
		playedBy = board.getGame().getCurrentPlayer();
		board.getGame().playedAt(this);
		return true;
	}
	
	public char getCharOutput() {
		if (playedBy == null || playedBy == TTPlayer.NONE)
			return isPlayable() ? '.' : ' ';
		if (playedBy == TTPlayer.XO)
			return '?';
		TTPlayer winner = board.getWonBy();
		return TTPlayer.isRealPlayer(winner) && !winner.equals(playedBy) 
				? '-' : playedBy.toString().charAt(0);
	}

	public int getGlobalX() {
		return board.getX() * board.getSize() + this.x;
	}
	public int getGlobalY() {
		return board.getY() * board.getSize() + this.y;
	}

	public TTBoard getBoard() {
		return board;
	}
	
	@Override
	public TTPlayer getWonBy() {
		return this.playedBy;
	}

	public TTBoard getDestinationBoard() {
		return board.getGame().getSub(x, y);
	}
	@Override
	public String toString() {
		return getGlobalX() + ", " + getGlobalY();
	}

	void reset() {
		this.playedBy = TTPlayer.NONE;
	}
}
