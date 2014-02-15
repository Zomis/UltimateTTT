package net.zomis.tttultimate.ais;

import java.util.Collection;

import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.tttultimate.TTBoard;
import net.zomis.tttultimate.TTPlayer;
import net.zomis.tttultimate.TTTUltimateGame;
import net.zomis.tttultimate.TTTile;
import net.zomis.tttultimate.TTWinCondition;

public class OpponentShouldNotPlayScorerV2 extends AbstractScorer<TTTUltimateGame, TTTile> {

	@Override
	public double getScoreFor(TTTile field, ScoreParameters<TTTUltimateGame> scores) {
		TTPlayer opponent = scores.getParameters().getCurrentPlayer().next();
		TTBoard sendToBoard = field.getDestinationBoard();
		
		Collection<TTWinCondition> colls = sendToBoard.getWinConds();
		double i = 0;
		for (TTWinCondition conds : colls) {
			i += conds.neededForWin(opponent);
		}
		return i;
	}

}
