package net.zomis.tttultimate.games;

import net.zomis.tttultimate.TTBase;
import net.zomis.tttultimate.TTPlayer;
import net.zomis.tttultimate.ais.NextPosFinder;

public class TTUltimateController extends TTController {
	
	private TTBase activeBoard = null;
	
	public TTUltimateController(TTBase board) {
		super(board);
	}

	private final NextPosFinder pos = new NextPosFinder();
	
	public TTBase getDestinationBoard(TTBase base) {
		return pos.getDestinationBoard(base);
	}

	@Override
	public boolean isAllowedPlay(TTBase tile) {
		TTBase parent = tile.getParent();
		TTBase grandpa = tile.getParent().getParent();
		
		if (!tile.getWonBy().equals(TTPlayer.NONE))
			return false;
		if (parent.getWonBy().isExactlyOnePlayer())
			return false;
		if (grandpa.isWon())
			return false;
		
		return activeBoard == null || activeBoard == parent || activeBoard.getWonBy() != TTPlayer.NONE;
	}

	@Override
	public boolean performPlay(TTBase tile) {
		tile.setPlayedBy(currentPlayer);
		activeBoard = getDestinationBoard(tile);
		nextPlayer();
		
		// Check for win condition on tile and if there is a win, cascade to it's parents
		do {
			tile.determineWinner();
			tile = tile.isWon() ? tile.getParent() : null;
		}
		while (tile != null);
		
		return true;
	}

	@Override
	protected void onReset() {
		this.activeBoard = null;
	}

}
