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
	
	public static <T> Collection<T> getAllSubs(HasSub<T> board) {
		List<T> list = new ArrayList<>();
		int sizeX = board.getSizeX();
		int sizeY = board.getSizeY();
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				list.add(board.getSub(x, y));
			}
		}
		return list;
	}
	public static Collection<TTBase> getAllSmallestFields(TTBase game) {
		Collection<TTBase> all = new ArrayList<>();
		
		for (TTBase sub : TicUtils.getAllSubs(game)) {
			if (sub.hasSubs())
				all.addAll(getAllSmallestFields(sub));
			else all.add(sub);
		}
		return all;
	}
	
	public static List<TTWinCondition> setupWins(final HasSub<? extends Winnable> board) {
		if (!board.hasSubs()) {
			ArrayList<TTWinCondition> list = new ArrayList<>();
			list.add(new TTWinCondition(board));
			return list;
		}
		
		int consecutive = board.getConsecutiveRequired();
		List<TTWinCondition> conds = new ArrayList<>();
		
		// Scan columns for a winner
		for (int xx = 0; xx < board.getSizeX(); xx++) {
			newWin(conds, consecutive, loopAdd(board, xx, 0, 0, 1));
		}
		
		// Scan rows for a winner
		for (int yy = 0; yy < board.getSizeY(); yy++) {
			newWin(conds, consecutive, loopAdd(board, 0, yy, 1, 0));
		}
		
		// Scan diagonals for a winner: Bottom-right
		for (int yy = 0; yy < board.getSizeY(); yy++) {
			newWin(conds, consecutive, loopAdd(board, 0, yy, 1, 1));
		}
		for (int xx = 1; xx < board.getSizeX(); xx++) {
			newWin(conds, consecutive, loopAdd(board, xx, 0, 1, 1));
		}
		
		// Scan diagonals for a winner: Bottom-left
		for (int xx = 0; xx < board.getSizeY(); xx++) {
			newWin(conds, consecutive, loopAdd(board, xx, 0, -1, 1));
		}
		for (int yy = 1; yy < board.getSizeY(); yy++) {
			newWin(conds, consecutive, loopAdd(board, board.getSizeX() - 1, yy, -1, 1));
		}
		
		return conds;
	}
	
	private static void newWin(List<TTWinCondition> conds, int consecutive, List<Winnable> winnables) {
		if (winnables.size() >= consecutive) // shorter win conditions doesn't need to be added as they will never be able to win
			conds.add(new TTWinCondition(winnables, consecutive));
	}

	private static List<Winnable> loopAdd(HasSub<? extends Winnable> board,
			int xx, int yy, int dx, int dy) {
		List<Winnable> winnables = new ArrayList<>();
		
		Winnable tile;
		do {
			tile = board.getSub(xx, yy);
			xx += dx;
			yy += dy;
			if (tile != null)
				winnables.add(tile);
		}
		while (tile != null);
		
		return winnables;
	}

}
