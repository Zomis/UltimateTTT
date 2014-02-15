package net.zomis.tttultimate;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TTWinCondition {

	private final Set<Winnable> winnables;

	public TTWinCondition(Winnable... winnables) {
		this(Arrays.asList(winnables));
	}
	public TTWinCondition(Collection<Winnable> winnables) {
		if (winnables.isEmpty())
			throw new IllegalArgumentException("Can't have an empty win condition!");
		this.winnables = new HashSet<>(winnables);
	}
	
	public int neededForWin(TTPlayer player) {
		return winnables.size() - hasCurrently(player);
	}
	public boolean isWinnable(TTPlayer byPlayer) {
		return hasCurrently(byPlayer.next()) == 0;
	}
	public int hasCurrently(TTPlayer player) {
		int i = 0;
		for (Winnable winnable : winnables) {
			if (winnable.getWonBy().and(player) == player)
				i++;
		}
		return i;
	}
	
	public TTPlayer determineWinner() {
		TTPlayer winner = TTPlayer.XO;
		for (Winnable winnable : winnables)
			winner = winner.and(winnable.getWonBy());
		return winner;
	}
	
	public boolean hasWinnable(Winnable field) {
		return winnables.contains(field);
	}
	
	public int size() {
		return winnables.size();
	}
	
}
