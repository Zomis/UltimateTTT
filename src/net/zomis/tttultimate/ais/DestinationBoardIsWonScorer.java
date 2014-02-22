package net.zomis.tttultimate.ais;

import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.tttultimate.dry.TTBase;
import net.zomis.tttultimate.dry.TTController;

public class DestinationBoardIsWonScorer extends AbstractScorer<TTController, TTBase> {

	@Override
	public double getScoreFor(TTBase field, ScoreParameters<TTController> scores) {
		TTBase sendToBoard = scores.getAnalyze(NextPosFinder.class).getDestinationBoard(field);
		return sendToBoard != null && !sendToBoard.isWon() ? 1 : 0;
	}

}
