package net.zomis.tttultimate.players;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.zomis.aiscores.ScoreConfig;
import net.zomis.aiscores.extra.GeneticInterface;
import net.zomis.aiscores.extra.ScoreGenetics;
import net.zomis.fight.FightResults;
import net.zomis.fight.GameFight;
import net.zomis.tttultimate.TTTUltimateGame;
import net.zomis.tttultimate.TTTile;

public class TTGeneticAI implements GeneticInterface<TTTUltimateGame, TTTile, TTAI> {
	private ScoreGenetics<TTTUltimateGame, TTTile, TTAI> genetics;
	private Random random = new Random();
	
	private List<TTAI> ais = new ArrayList<>();
	
	public TTGeneticAI() {
		genetics = new ScoreGenetics<TTTUltimateGame, TTTile, TTAI>(this, random);
		
		ais.add(TTAIFactory.improved3().build());
		ais.add(TTAIFactory.version3().build());
		ais.add(TTAIFactory.version2().build());
		ais.add(TTAIFactory.versionOne().build());
	}

	@Override
	public ScoreConfig<TTTUltimateGame, TTTile> getConfigFor(TTAI c) {
		return c.getConfig();
	}

	@Override
	public ScoreConfig<TTTUltimateGame, TTTile> newConfig() {
		return TTAIFactory.randomAllIn().build().getConfig();
	}

	@Override
	public TTAI newFromConfig(ScoreConfig<TTTUltimateGame, TTTile> config) {
		return new TTAI("Genetic", config);
	}

	@Override
	public Map<TTAI, Double> fitness(List<TTAI> ais) {
		GameFight<TTAI> fight = new GameFight<>();
		FightResults<TTAI> result = fight.fightEvenly(ais.toArray(new TTAI[ais.size()]), 1000, new TTAIFactory.FightImpl());
		System.out.println(result.toStringMultiLine());
		return result.getPercentagesDesc();
	}
	
	public void evolve() {
		ais = this.genetics.iterationFight(ais);
	}
	
}
