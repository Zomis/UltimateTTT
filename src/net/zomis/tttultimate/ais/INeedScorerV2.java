package net.zomis.tttultimate.ais;

import java.util.Collection;

import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.tttultimate.TTPlayer;
import net.zomis.tttultimate.TTTUltimateGame;
import net.zomis.tttultimate.TTTile;
import net.zomis.tttultimate.TTWinCondition;
import net.zomis.tttultimate.TicUtils;

public class INeedScorerV2 extends AbstractScorer<TTTUltimateGame, TTTile> {

	@Override
	public double getScoreFor(TTTile field, ScoreParameters<TTTUltimateGame> scores) {
		TTPlayer currentPlayer = scores.getParameters().getCurrentPlayer();
		Collection<TTWinCondition> colls = TicUtils.getWinCondsWith(field, field.getBoard());
		double i = 0;
		for (TTWinCondition conds : colls) {
			if (conds.isWinnable(currentPlayer))
				i += conds.hasCurrently(currentPlayer);
		}
		return i;
	}

}
