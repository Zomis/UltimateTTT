package net.zomis.tttultimate.ais;

import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.tttultimate.dry.TTBase;
import net.zomis.tttultimate.dry.TTController;

public class ImportantForMe extends AbstractScorer<TTController, TTBase> {

	@Override
	public double getScoreFor(TTBase field, ScoreParameters<TTController> scores) {
		return scores.getAnalyze(BoardImportanceAnalyze.BoardImportance.class)
				.getImportanceFor(field.getParent(), scores.getParameters().getCurrentPlayer());
	}

}
