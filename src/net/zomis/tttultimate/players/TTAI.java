package net.zomis.tttultimate.players;

import java.util.Collection;
import java.util.Random;
import java.util.Map.Entry;

import net.zomis.aiscores.FieldScore;
import net.zomis.aiscores.FieldScoreProducer;
import net.zomis.aiscores.FieldScores;
import net.zomis.aiscores.ScoreConfig;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.aiscores.ScoreStrategy;
import net.zomis.aiscores.extra.ParamAndField;
import net.zomis.aiscores.extra.ScoreUtils;
import net.zomis.tttultimate.TTTUltimateGame;
import net.zomis.tttultimate.TTTile;

public class TTAI implements ScoreStrategy<TTTUltimateGame, TTTile> {

	private final ScoreConfig<TTTUltimateGame, TTTile> config;
	private FieldScoreProducer<TTTUltimateGame, TTTile>	producer;
	private Random	random = new Random();
	private final String	name;

	public TTAI(String name, ScoreConfig<TTTUltimateGame, TTTile> config) {
		this.name = name;
		this.config = config;
		this.producer = new FieldScoreProducer<TTTUltimateGame, TTTile>(this.config, this);
	}
	
	public TTTile play(TTTUltimateGame board) {
		ParamAndField<TTTUltimateGame, TTTile> ff = ScoreUtils.pickBest(producer, board, random);
		if (ff == null)
			return null;
		return ff.getField();
	}

	public FieldScores<TTTUltimateGame, TTTile> score(TTTUltimateGame board) {
		producer.setDetailed(true);
		FieldScores<TTTUltimateGame, TTTile> scores = producer.analyzeAndScore(board);
		producer.setDetailed(false);
		return scores;
	}
	public FieldScoreProducer<TTTUltimateGame, TTTile> getProducer() {
		return producer;
	}
	
	@Override
	public Collection<TTTile> getFieldsToScore(TTTUltimateGame params) {
		return params.getAllFields();
	}

	@Override
	public boolean canScoreField(ScoreParameters<TTTUltimateGame> parameters, TTTile field) {
		return field.isPlayable();
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public String getName() {
		return name;
	}
	
	public void logScores(TTTUltimateGame board) {
		FieldScores<TTTUltimateGame, TTTile> scores = this.score(board);
		for (Entry<TTTile, FieldScore<TTTile>> ee : scores.getScores().entrySet()) {
			System.out.println("Score for " + ee.getKey());
			FieldScore<TTTile> value = ee.getValue();
			System.out.println("Normalized: " + value.getNormalized());
			System.out.println("Rank: " + value.getRank());
			System.out.println("Details: " + value.getScoreMap());
			System.out.println();
		}
	}

	public ScoreConfig<TTTUltimateGame, TTTile> getConfig() {
		return this.config;
	}
}
