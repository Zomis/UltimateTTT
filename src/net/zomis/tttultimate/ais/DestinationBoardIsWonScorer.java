package net.zomis.tttultimate.ais;

import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.tttultimate.TTBoard;
import net.zomis.tttultimate.TTTUltimateGame;
import net.zomis.tttultimate.TTTile;
import net.zomis.tttultimate.TicUtils;

public class DestinationBoardIsWonScorer extends AbstractScorer<TTTUltimateGame, TTTile> {

	@Override
	public double getScoreFor(TTTile field, ScoreParameters<TTTUltimateGame> scores) {
		TTBoard sendToBoard = field.getDestinationBoard();
		return TicUtils.isWon(sendToBoard) ? 1 : 0;
	}

}
