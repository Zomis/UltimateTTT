package net.zomis.tttultimate.dry;

import java.util.List;

import net.zomis.tttultimate.HasSub;
import net.zomis.tttultimate.TTPlayer;
import net.zomis.tttultimate.TTWinCondition;
import net.zomis.tttultimate.Winnable;

public class TTBase implements Winnable, HasSub<TTBase> {
	
	private final TTBase[][] subs;
	private final List<TTWinCondition> winConditions;
	private TTPlayer playedBy = TTPlayer.NONE;
	private final TTBase parent;
	private final TTMNKParameters mnkParams;
	private final int x;
	private final int y;
	
	public TTBase(TTBase parent, TTMNKParameters parameters, TTFactory factory) {
		this(parent, 0, 0, parameters, factory);
	}
	public TTBase(TTBase parent, int x, int y, TTMNKParameters parameters, TTFactory factory) {
		this.parent = parent;
		this.mnkParams = parameters;
		this.x = x;
		this.y = y;
		
		this.subs = new TTBase[parameters.getWidth()][parameters.getHeight()];
		for (int xx = 0; xx < parameters.getWidth(); xx++) {
			for (int yy = 0; yy < parameters.getHeight(); yy++) {
				this.subs[xx][yy] = factory.construct(this, xx, yy);
			}
		}
		this.winConditions = TTUtils2.setupWins(this);
	}
	
	protected TTPlayer determineWinner() {
		TTPlayer winner = TTPlayer.NONE;
		for (TTWinCondition cond : this.winConditions) {
			winner = winner.or(cond.determineWinnerNew());
		}
		this.playedBy = winner;
		return winner;
	}
	
	@Override
	public TTBase getSub(int x, int y) {
		if (!hasSubs() && x == 0 && y == 0)
			return this;
		if (x < 0 || y < 0)
			return null;
		if (x >= getSizeX() || y >= getSizeY())
			return null;
		return subs[x][y];
	}

	@Override
	public Iterable<TTWinCondition> getWinConds() {
		return winConditions;
	}

	@Override
	public TTPlayer getWonBy() {
		return this.playedBy;
	}

	@Override
	public int getSizeX() {
		return this.mnkParams.getWidth();
	}

	@Override
	public int getSizeY() {
		return this.mnkParams.getHeight();
	}

	@Override
	public int getConsecutiveRequired() {
		return this.mnkParams.getConsecutiveRequired();
	}

	public TTMNKParameters getMNKParameters() {
		return this.mnkParams;
	}

	public TTBase getParent() {
		return parent;
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
	public TTBase getSmallestTile(int x, int y) {
		// TODO: This is probably not optimal. Works best for boards where all dimensions have the same size. Might work for others too though.
		TTBase board = getSub(x / getSizeX(), y / getSizeY());
		return board.getSub(x % getSizeX(), y % getSizeY());
	}
	
	public TTBase getSibling(int deltaX, int deltaY) {
		if (parent == null)
			return null;
		return parent.getSub(x + deltaX, y + deltaY);
	}
	
	public boolean isGameOver() {
		return TTPlayer.isExactlyOnePlayer(playedBy);
	}
	
	public void setPlayedBy(TTPlayer playedBy) {
		this.playedBy = playedBy;
	}
	
	public boolean hasSubs() {
		return getSizeX() != 0 && getSizeY() != 0;
	}
	
	@Override
	public String toString() {
		// Can't use String.format because of compability with GWT
		return "{Pos " + x + ", " + y + "; Size " + getSizeX() + ", " + getSizeY() + "; Played by " + getWonBy() + "}";
	}
	
}
