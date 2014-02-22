package net.zomis.tttultimate;

public interface TTFactory {
	TTBase construct(TTBase parent, int x, int y);
}
