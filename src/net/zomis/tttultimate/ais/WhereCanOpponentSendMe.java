package net.zomis.tttultimate.ais;

import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.tttultimate.TTTUltimateGame;
import net.zomis.tttultimate.TTTile;

public class WhereCanOpponentSendMe extends AbstractScorer<TTTUltimateGame, TTTile> {

	@Override
	public double getScoreFor(TTTile field, ScoreParameters<TTTUltimateGame> scores) {
		// TODO: Check which fields opponent can send me to on the target board, and check how much I want to play on those fields.
		 // Could there be a recursive approach for this? A deeper expected value calculation?
		
		return 0;
	}

}
