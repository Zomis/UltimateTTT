package net.zomis.tttultimate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TicUtils {
	public static <E extends Winnable> Collection<TTWinCondition> getWinCondsWith(E field, HasSub<E> board) {
		Collection<TTWinCondition> coll = new ArrayList<>();
		for (TTWinCondition cond : board.getWinConds()) {
			if (cond.hasWinnable(field))
				coll.add(cond);
		}
		return coll;
	}
	
	public static <T> Collection<T> getTiles(HasSub<T> board) {
		List<T> list = new ArrayList<>();
		int size = board.getSize();
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				list.add(board.getSub(x, y));
			}
		}
		return list;
	}
	public static List<Winnable> emptyWinnables(HasSub<?> board) {
		return new ArrayList<>(board.getSize());
	}
	public static <T extends Winnable> List<TTWinCondition> setupWins(HasSub<T> board) {
		List<Winnable> winnables;
		int size = board.getSize();
		List<TTWinCondition> conds = new ArrayList<>();
		for (int xx = 0; xx < board.getSize(); xx++) {
			// Scan columns for a winner
			winnables = emptyWinnables(board);
			for (int yy = 0; yy < board.getSize(); yy++) {
				winnables.add(board.getSub(xx, yy));
			}
			conds.add(new TTWinCondition(winnables));
			
			// Scan rows for a winner
			winnables = emptyWinnables(board);
			for (int yy = 0; yy < size; yy++) {
				winnables.add(board.getSub(yy, xx));
			}
			conds.add(new TTWinCondition(winnables));
		}
		
		// Scan diagonals for a winner
		winnables = emptyWinnables(board);
		for (int i = 0; i < board.getSize(); i++)
			winnables.add(board.getSub(i, i));
		conds.add(new TTWinCondition(winnables));
		
		winnables = emptyWinnables(board);
		for (int i = 0; i < board.getSize(); i++)
			winnables.add(board.getSub(size - i - 1, i));
		conds.add(new TTWinCondition(winnables));
		
		return conds;
	}

	public static boolean isWon(Winnable winnable) {
		return winnable.getWonBy() != TTPlayer.NONE;
	}

}
