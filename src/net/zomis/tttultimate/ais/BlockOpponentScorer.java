package net.zomis.tttultimate.ais;

import java.util.Collection;

import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.tttultimate.TTPlayer;
import net.zomis.tttultimate.TTTUltimateGame;
import net.zomis.tttultimate.TTTile;
import net.zomis.tttultimate.TTWinCondition;

public class BlockOpponentScorer extends AbstractScorer<TTTUltimateGame, TTTile> {

	@Override
	public double getScoreFor(TTTile field, ScoreParameters<TTTUltimateGame> scores) {
		Collection<TTWinCondition> conds = field.getBoard().getWinConds();
		TTPlayer player = scores.getParameters().getCurrentPlayer().next();
		double has = 0;
		for (TTWinCondition cond : conds) {
			if (!cond.isWinnable(player))
				continue;
			if (!cond.hasWinnable(field))
				continue;
			int currently = cond.hasCurrently(player);
			double max = cond.size();
			has = Math.max(has, (double) currently / max);
		}
		return has;
	}

}
