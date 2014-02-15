package net.zomis.tttultimate.ais;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.zomis.aiscores.PreScorer;
import net.zomis.tttultimate.TTBoard;
import net.zomis.tttultimate.TTPlayer;
import net.zomis.tttultimate.TTTUltimateGame;
import net.zomis.tttultimate.TTWinCondition;
import net.zomis.tttultimate.TicUtils;

public class BoardImportanceAnalyze implements PreScorer<TTTUltimateGame> {
	// x AnalyzeHowImportantBoardIs for winning (X, O separately) -- especially in combination with INeedScorer

	@Override
	public Object analyze(TTTUltimateGame params) {
		Map<TTBoard, Map<TTPlayer, Double>> importance = new HashMap<>();
		
		for (TTBoard board : TicUtils.getTiles(params)) {
			Map<TTPlayer, Double> map = new HashMap<>();
			for (TTPlayer player : TTPlayer.values()) {
				if (!player.isExactlyOnePlayer())
					continue;
				
				double dd = 0;
				for (TTWinCondition win : TicUtils.getWinCondsWith(board, board.getGame())) {
					if (win.isWinnable(player)) {
						dd += 0.5 + win.hasCurrently(player);
					}
				}
				map.put(player, dd);
			}
			importance.put(board, map);
		}
		
		return new BoardImportance(importance);
	}

	@Override
	public void onScoringComplete() {
	}
	
	public static class BoardImportance {
		private final Map<TTBoard, Map<TTPlayer, Double>> map;

		private BoardImportance(Map<TTBoard, Map<TTPlayer, Double>> data) {
			this.map = Collections.unmodifiableMap(data);
		}
		public Map<TTBoard, Map<TTPlayer, Double>> getMap() {
			return map;
		}
		public double getImportanceFor(TTBoard board, TTPlayer player) {
			return map.get(board).get(player);
		}
	}

}
