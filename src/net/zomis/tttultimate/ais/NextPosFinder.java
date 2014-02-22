package net.zomis.tttultimate.ais;

import net.zomis.aiscores.PreScorer;
import net.zomis.tttultimate.TTBase;
import net.zomis.tttultimate.games.TTController;

public class NextPosFinder implements PreScorer<TTController> {

	@Override
	public Object analyze(TTController params) {
		return this;
	}

	public TTBase getDestinationBoard(TTBase tile) {
		TTBase parent = tile.getParent();
		if (parent == null)
			return null;
		TTBase grandpa = parent.getParent();
		if (grandpa == null)
			return null;
		return grandpa.getSub(tile.getX(), tile.getY());
	}
	
	@Override
	public void onScoringComplete() {
	}

}
