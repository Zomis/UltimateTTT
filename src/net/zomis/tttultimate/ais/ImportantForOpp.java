package net.zomis.tttultimate.ais;

import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.tttultimate.TTTUltimateGame;
import net.zomis.tttultimate.TTTile;

public class ImportantForOpp extends AbstractScorer<TTTUltimateGame, TTTile> {

	@Override
	public double getScoreFor(TTTile field, ScoreParameters<TTTUltimateGame> scores) {
		return scores.getAnalyze(BoardImportanceAnalyze.BoardImportance.class)
				.getImportanceFor(field.getBoard(), scores.getParameters().getCurrentPlayer().next());
	}

}
