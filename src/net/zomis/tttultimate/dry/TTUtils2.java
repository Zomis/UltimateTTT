package net.zomis.tttultimate.dry;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import net.zomis.tttultimate.HasSub;
import net.zomis.tttultimate.TTWinCondition;
import net.zomis.tttultimate.Winnable;

public class TTUtils2 {

	private static List<Winnable> emptyWinnables(HasSub<?> board) {
		return new ArrayList<>(board.getSizeX());
	}
	public static List<TTWinCondition> setupWins(final TTBase board) {
		if (!board.hasSubs()) {
			return Lists.newArrayList(new TTWinCondition(board));
		}
		
		int consecutive = board.getConsecutiveRequired();
		
		List<Winnable> winnables;
		if (board.getSizeX() != board.getSizeY())
			throw new IllegalArgumentException("This method only works for quadratic boards");
		
		int size = board.getSizeX();
		
		List<TTWinCondition> conds = new ArrayList<>();
		for (int xx = 0; xx < board.getSizeX(); xx++) {
			// Scan columns for a winner
			winnables = emptyWinnables(board);
			for (int yy = 0; yy < board.getSizeY(); yy++) {
				winnables.add(board.getSub(xx, yy));
			}
			conds.add(new TTWinCondition(winnables, consecutive));
			
			// Scan rows for a winner
			winnables = emptyWinnables(board);
			for (int yy = 0; yy < board.getSizeX(); yy++) {
				winnables.add(board.getSub(yy, xx));
			}
			conds.add(new TTWinCondition(winnables, consecutive));
		}
		
		// Scan diagonals for a winner
		winnables = emptyWinnables(board);
		for (int i = 0; i < size; i++)
			winnables.add(board.getSub(i, i));
		if (winnables.isEmpty())
			throw new IllegalStateException("" + board);
		conds.add(new TTWinCondition(winnables, consecutive));
		
		winnables = emptyWinnables(board);
		for (int i = 0; i < size; i++)
			winnables.add(board.getSub(size - i - 1, i));
		conds.add(new TTWinCondition(winnables, consecutive));
		
		return conds;
	}

}
