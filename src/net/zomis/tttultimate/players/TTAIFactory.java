package net.zomis.tttultimate.players;

import java.util.Random;

import net.zomis.aiscores.PreScorer;
import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.aiscores.scorers.Scorers;
import net.zomis.fight.FightInterface;
import net.zomis.tttultimate.TTPlayer;
import net.zomis.tttultimate.ais.BlockOpponentScorer;
import net.zomis.tttultimate.ais.BoardImportanceAnalyze;
import net.zomis.tttultimate.ais.DestinationBoardIsWonScorer;
import net.zomis.tttultimate.ais.INeedScorerV1;
import net.zomis.tttultimate.ais.INeedScorerV2;
import net.zomis.tttultimate.ais.INeedScorerV3;
import net.zomis.tttultimate.ais.INeedScorerV4;
import net.zomis.tttultimate.ais.ImportantForMe;
import net.zomis.tttultimate.ais.NextPosFinder;
import net.zomis.tttultimate.ais.OpponentShouldNotPlayScorerV1;
import net.zomis.tttultimate.ais.OpponentShouldNotPlayScorerV2;
import net.zomis.tttultimate.ais.OpponentShouldNotPlayScorerV3;
import net.zomis.tttultimate.ais.WhereCanOpponentSendMe;
import net.zomis.tttultimate.dry.TTBase;
import net.zomis.tttultimate.dry.TTController;
import net.zomis.tttultimate.dry.TTFactoryImpl;
import net.zomis.tttultimate.dry.TTUltimateController;

public class TTAIFactory {
	private final String	name;
	private final ScoreConfigFactory<TTController, TTBase>	factory;

	private TTAIFactory(String name, ScoreConfigFactory<TTController, TTBase> factory) {
		this.name = name;
		this.factory = factory;
	}
	
	public TTAI build() {
		return new TTAI(name, factory.build());
	}
	private ScoreConfigFactory<TTController, TTBase> copy() {
		return factory.copy();
	}

	public static TTAIFactory random() {
		return new TTAIFactory("#AI_Complete_Idiot", new ScoreConfigFactory<TTController, TTBase>()
				.withPreScorer(new NextPosFinder())
		);
	}
	public static TTAIFactory versionOne() {
		return new TTAIFactory("#AI_First", 
				random().copy()
				.withScorer(new INeedScorerV1())
		);
	}
	public static TTAIFactory version2() {
		return new TTAIFactory("#AI_Second", 
				versionOne().copy()
				.withPreScorer(new PreScorer<TTController>() {
					@Override
					public void onScoringComplete() {
					}
					@Override
					public Object analyze(TTController params) {
						return new Object();
					}
				})
				.withScorer(new OpponentShouldNotPlayScorerV1())
		);
	}
	public static TTAIFactory version3() {
		return new TTAIFactory("#AI_Third", 
				random().copy()
				.withScorer(new INeedScorerV2())
				.withScorer(new OpponentShouldNotPlayScorerV2())
		);
	}
	public static TTAIFactory improved3() {
		return new TTAIFactory("#AI_Medium", 
				versionOne().copy()
				.withPreScorer(new BoardImportanceAnalyze())
				.withScorer(new INeedScorerV3())
				.withScorer(new OpponentShouldNotPlayScorerV3())
				.withScorer(new WhereCanOpponentSendMe())
				.withScorer(new DestinationBoardIsWonScorer())
				.withScorer(new BlockOpponentScorer())
		);
	}
	public static TTAIFactory unreleased() {
		return new TTAIFactory("#AI_Unreleased", random().copy()
				.withPreScorer(new BoardImportanceAnalyze())
				
				.withScorer(Scorers.normalized(Scorers.multiplication(new ImportantForMe(), new INeedScorerV4())))
				.withScorer(new OpponentShouldNotPlayScorerV3())
				.withScorer(new WhereCanOpponentSendMe())
				.withScorer(new DestinationBoardIsWonScorer(), 0.7)
				.withScorer(new BlockOpponentScorer(), 1.3)
		);
	}

	public static class FightImpl implements FightInterface<TTAI> {
		@Override
		public TTAI determineWinner(TTAI[] players, int fightNumber) {
			TTBase board = new TTFactoryImpl().ultimate();
			TTController game = new TTUltimateController(board);
			while (!game.isGameOver()) {
				TTAI pl = playerFor(players, game.getCurrentPlayer());
				TTBase choice = pl.play(game);
				if (choice != null)
					game.play(choice);
				else break;
			}
			if (!game.isGameOver()) // game has ended in a draw
				return null;
			return playerFor(players, game.getWonBy());
		}
		private static <E> E playerFor(E[] players, TTPlayer player) {
			switch (player) {
				case X:
					return players[0];
				case O:
					return players[1];
				default:
					throw new IllegalStateException("Unexpected player " + player);
			}
		}
	}

	public static TTAIFactory best() {
		return unreleased(); // improved3();
	}

	private static Random random = new Random();
	public static TTAIFactory randomAllIn() {
		long seed = random.nextLong();
		Random random = new Random(seed);
		return new TTAIFactory("Random" + seed,
				random().copy()
				.withPreScorer(new BoardImportanceAnalyze())
				.withScorer(new INeedScorerV1(), r(random))
				.withScorer(new INeedScorerV2(), r(random))
				.withScorer(new INeedScorerV3(), r(random))
				.withScorer(new OpponentShouldNotPlayScorerV1(), r(random))
				.withScorer(new OpponentShouldNotPlayScorerV2(), r(random))
				.withScorer(new OpponentShouldNotPlayScorerV3(), r(random))
				.withScorer(new BlockOpponentScorer(), r(random))
				.withScorer(new DestinationBoardIsWonScorer(), r(random))
				.withScorer(new WhereCanOpponentSendMe(), r(random))
		);
	}

	private static double r(Random random) {
		return random.nextDouble();
	}

}
