package net.zomis.tttultimate.ais;

import java.util.Collection;

import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.tttultimate.TTBoard;
import net.zomis.tttultimate.TTPlayer;
import net.zomis.tttultimate.TTTUltimateGame;
import net.zomis.tttultimate.TTTile;
import net.zomis.tttultimate.TTWinCondition;
import net.zomis.tttultimate.TicUtils;

public class OpponentShouldNotPlayScorerV3 extends AbstractScorer<TTTUltimateGame, TTTile> {

	@Override
	public double getScoreFor(TTTile field, ScoreParameters<TTTUltimateGame> scores) {
		TTPlayer opponent = scores.getParameters().getCurrentPlayer().next();
		TTBoard sendToBoard = field.getDestinationBoard();
		if (TicUtils.isWon(sendToBoard)) // board is won, then opponent should definitely not play there. Let another scorer handle this case
			return 0;
		
		Collection<TTWinCondition> colls = sendToBoard.getWinConds();
		double i = 0;
		for (TTWinCondition conds : colls) {
			i += conds.neededForWin(opponent);
		}
		// x DestinationBoardIsWonScorer
		return i / colls.size();
	}

}
