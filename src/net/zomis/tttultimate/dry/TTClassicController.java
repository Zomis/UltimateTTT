package net.zomis.tttultimate.dry;

import net.zomis.tttultimate.TTPlayer;

public class TTClassicController extends TTController {

	public TTClassicController(TTBase board) {
		super(board);
	}

	@Override
	public boolean isAllowedPlay(TTBase tile) {
		return tile.getParent() != null && 
				tile.getParent().getWonBy().equals(TTPlayer.NONE) &&
				tile.getWonBy() == TTPlayer.NONE;
	}

	@Override
	public boolean performPlay(TTBase tile) {
		tile.setPlayedBy(currentPlayer);
		nextPlayer();
		return true;
	}

	@Override
	protected void onPlay(TTBase tile) {
	}

}
