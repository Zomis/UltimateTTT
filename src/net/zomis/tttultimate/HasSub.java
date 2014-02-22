package net.zomis.tttultimate;

public interface HasSub<T> extends Winnable {
	T getSub(int x, int y);
	Iterable<TTWinCondition> getWinConds();
	int getSizeX();
	int getSizeY();
	int getConsecutiveRequired();
	boolean hasSubs();
}
