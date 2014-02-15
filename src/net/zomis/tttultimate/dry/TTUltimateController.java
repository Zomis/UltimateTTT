package net.zomis.tttultimate.dry;

import net.zomis.tttultimate.TTPlayer;

public class TTUltimateController extends TTController {
	
	private TTBase activeBoard = null;
	
	public TTUltimateController(TTBase board) {
		super(board);
	}

	public TTBase getDestinationBoard(TTBase base) {
		TTBase parent = base.getParent();
		if (parent == null)
			return null;
		TTBase grandpa = parent.getParent();
		if (grandpa == null)
			return null;
		return grandpa.getSub(base.getX(), base.getY());
	}

	@Override
	public boolean isAllowedPlay(TTBase tile) {
		TTBase parent = tile.getParent();
		TTBase grandpa = tile.getParent().getParent();
		
		if (!tile.getWonBy().equals(TTPlayer.NONE))
			return false;
		if (parent.getWonBy().isExactlyOnePlayer())
			return false;
		if (grandpa.isGameOver())
			return false;
		
		return activeBoard == null || activeBoard == parent || activeBoard.getWonBy() != TTPlayer.NONE;
	}

	@Override
	public boolean performPlay(TTBase tile) {
		tile.setPlayedBy(currentPlayer);
		activeBoard = getDestinationBoard(tile);
		nextPlayer();
		return true;
	}

	@Override
	protected void onPlay(TTBase tile) {
	}
	

}
